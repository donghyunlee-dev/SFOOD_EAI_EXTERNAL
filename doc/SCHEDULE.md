# Quartz Job Schedule

이 문서는 애플리케이션에서 Quartz 스케줄러가 동작하는 과정을 설명합니다.

## 1. 스케줄러 초기화 단계

### 1.1 스케줄러 생성 및 주입
```java
public SabangNetScheduleRegister(MarketMallService mallService, @Qualifier("sabangNetScheduler") Scheduler scheduler) {
    this.mallService = mallService;
    this.scheduler = scheduler;
}
```
- 스프링의 DI(의존성 주입)를 통해 `@Qualifier("sabangNetScheduler")` 어노테이션으로 지정된 Quartz 스케줄러가 주입됩니다.

### 1.2 스케줄러 시작
```java
@PostConstruct
public void initialize() {
    try {
        scheduler.start();
    } catch (SchedulerException e) {
        log.error(e.getMessage());
    }
}
```
- 스프링 컨테이너가 빈을 완전히 초기화한 후 `@PostConstruct` 어노테이션이 붙은 `initialize()` 메서드가 호출됩니다.
- 이 메서드에서 `scheduler.start()`를 호출하여 Quartz 스케줄러를 시작합니다.

## 2. 스케줄 등록 및 갱신 단계

### 2.1 스케줄 갱신 트리거
```java
@Scheduled(cron = "0 */1 * * * *")
private void refreshScheduledBatchJobs() throws SchedulerException {
    refreshSchedules();
}
```
- 스프링의 `@Scheduled` 어노테이션을 사용하여 1분마다 `refreshScheduledBatchJobs()` 메서드가 실행됩니다.
- 이 메서드는 내부적으로 `refreshSchedules()` 메서드를 호출합니다.

### 2.2 스케줄 정보 조회
```java
for (CollectSchedule schedule : mallService.findScheduleByChannel(CHANNEL_CODE)) {
    // 스케줄 처리 로직
}
```
- `mallService.findScheduleByChannel(CHANNEL_CODE)`를 호출하여 특정 채널에 대한 스케줄 정보를 조회합니다.
- 이 정보는 DB에서 가져오거나 JSON 파일에서 로드될 수 있습니다(예: `schedules.json` 파일).

### 2.3 Job 및 Trigger 키 생성
```java
String groupId = schedule.getId().getCompanyCode() + "." + CHANNEL_CODE;
JobKey jobkey = JobKey.jobKey(schedule.getMallCode() + "." + schedule.getCollectType(), groupId);
```
- 각 Job에 대한 고유 식별자(JobKey)를 생성합니다.
- `SabangNetScheduleRegister`에서는 `mallCode + "." + collectType`을 Job 이름으로 사용합니다.
- `RetailScheduleRegister`에서는 `mallCode + "." + inMallCode + "." + collectType`을 Job 이름으로 사용합니다.

### 2.4 Job 존재 여부 확인 및 처리
```java
if (scheduler.checkExists(jobkey)) {
    // 기존 Job 처리 로직
} else {
    // 신규 Job 등록 로직
}
```
- `scheduler.checkExists(jobkey)`를 통해 해당 Job이 이미 등록되어 있는지 확인합니다.

### 2.5 기존 Job 처리
기존 Job이 존재하는 경우:
```java
TriggerKey triggerKey = TriggerKey.triggerKey(String.valueOf(schedule.getId().getCollectScheduleSeq()), groupId);

if (scheduler.checkExists(triggerKey)) {
    // 기존 Trigger 처리 로직
} else {
    // 새 Trigger 등록 로직
    scheduler.scheduleJob(QuartzUtils.addTrigger(schedule, triggerKey, jobkey));
}
```
- 트리거 키를 생성하고 해당 트리거가 존재하는지 확인합니다.
- 트리거가 존재하면 상태를 확인하고 필요에 따라 업데이트합니다.
- 트리거가 존재하지 않으면 새 트리거를 생성하여 기존 Job에 연결합니다.

### 2.6 트리거 상태 및 Cron 표현식 확인
```java
Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);

if ("Y".equalsIgnoreCase(schedule.getCollectYn())) {
    if (triggerState == Trigger.TriggerState.PAUSED) {
        scheduler.resumeTrigger(triggerKey);
    }

    Trigger existTrigger = scheduler.getTrigger(triggerKey);
    if (!((CronTrigger) existTrigger).getCronExpression().equals(schedule.getCronExpression())) {
        CronTrigger modifiedTrigger = QuartzUtils.addTrigger(schedule, triggerKey, existTrigger.getJobKey());
        scheduler.rescheduleJob(triggerKey, modifiedTrigger);
    }
} else {
    if (triggerState != Trigger.TriggerState.PAUSED) {
        scheduler.pauseTrigger(triggerKey);
    }
}
```
- 트리거 상태를 확인하고 `collectYn` 값에 따라 트리거를 활성화하거나 일시 중지합니다.
- Cron 표현식이 변경된 경우 트리거를 재스케줄링합니다.

### 2.7 신규 Job 등록
Job이 존재하지 않는 경우:
```java
if ("Y".equalsIgnoreCase(schedule.getCollectYn())) {
    JobDetail jobDetail = QuartzUtils.newJobDetail(schedule, groupId, SabangNetJob.class);
    Trigger trigger = QuartzUtils.newTrigger(schedule, groupId);
    scheduler.scheduleJob(jobDetail, trigger);
}
```
- `collectYn`이 "Y"인 경우에만 새 Job을 등록합니다.
- `QuartzUtils.newJobDetail()`을 사용하여 JobDetail을 생성합니다.
- `QuartzUtils.newTrigger()`를 사용하여 Trigger를 생성합니다.
- `scheduler.scheduleJob()`을 호출하여 Job과 Trigger를 스케줄러에 등록합니다.

## 3. Job 실행 단계

### 3.1 트리거 발동
- 등록된 Cron 표현식에 따라 Quartz 스케줄러가 트리거를 발동시킵니다.
- 예를 들어, `"0 15 8 * * ?"`는 매일 오전 8시 15분에 트리거가 발동됩니다.

### 3.2 Job 클래스 인스턴스화 및 실행
- 트리거가 발동되면 Quartz는 해당 Job 클래스(예: `SabangNetJob` 또는 `RetailJob`)의 인스턴스를 생성합니다.
- Job 클래스의 `execute()` 메서드가 호출됩니다.

### 3.3 JobDataMap을 통한 데이터 전달
```java
.usingJobData("companyCode", schedule.getId().getCompanyCode())
.usingJobData("mallCode", schedule.getMallCode())
.usingJobData("inMallCode", schedule.getInMallCode())
.usingJobData("collectType", schedule.getCollectType())
```
- JobDetail 생성 시 설정된 데이터(companyCode, mallCode, inMallCode, collectType)가 JobDataMap을 통해 Job 실행 시 전달됩니다.
- Job 클래스에서는 이 데이터를 사용하여 실제 작업을 수행합니다.

## 4. 주요 유틸리티 메서드 분석

### 4.1 JobDetail 생성
```java
public static JobDetail newJobDetail(CollectSchedule schedule, String groupName, Class clazz) {
    String jobName;

    if (StringUtils.hasText(schedule.getInMallCode())) {
        jobName = schedule.getMallCode() + "." + schedule.getInMallCode() + "." + schedule.getCollectType();
    } else {
        jobName = schedule.getMallCode() + "." + schedule.getCollectType();
    }

    return JobBuilder.newJob(clazz)
            .withIdentity(jobName, groupName)
            .usingJobData("companyCode", schedule.getId().getCompanyCode())
            .usingJobData("mallCode", schedule.getMallCode())
            .usingJobData("inMallCode", schedule.getInMallCode())
            .usingJobData("collectType", schedule.getCollectType())
            .build();
}
```
- Job 이름은 `inMallCode` 유무에 따라 다르게 생성됩니다.
- Job 클래스, 이름, 그룹, 데이터를 설정하여 JobDetail 객체를 생성합니다.

### 4.2 Trigger 생성
```java
public static CronTrigger newTrigger(CollectSchedule schedule, String groupName) {
    return TriggerBuilder.newTrigger()
            .withIdentity(String.valueOf(schedule.getId().getCollectScheduleSeq()), groupName)
            .withSchedule(CronScheduleBuilder.cronSchedule(schedule.getCronExpression()).inTimeZone(TimeZone.getTimeZone("Asia/Seoul")))
            .build();
}
```
- 트리거 이름은 `collectScheduleSeq`를 사용합니다.
- Cron 표현식과 시간대(Asia/Seoul)를 설정하여 CronTrigger 객체를 생성합니다.

## 5. 스케줄러 동작 방식 이해

### 5.1 Spring `@Scheduled`와 Quartz 스케줄러의 차이

애플리케이션에는 두 가지 유형의 스케줄러가 있습니다:

1. **Spring의 `@Scheduled` 어노테이션을 사용하는 스케줄러**:
   - `BatchScheduler` 클래스의 모든 메서드
   - `SabangNetScheduleRegister`와 `RetailScheduleRegister`의 `refreshScheduledBatchJobs()` 메서드

2. **Quartz를 사용한 동적 스케줄러**:
   - `SabangNetScheduleRegister`와 `RetailScheduleRegister`에서 관리하는 작업들

### 5.2 헬스체크와 스케줄러 실행의 관계

`BatchScheduler` 클래스에서는 모든 스케줄 작업 실행 전에 헬스체크를 수행합니다:
```java
if (!appServerManager.isActive()) return;
```
## 6. 요약: Quartz Job 실행 순서

1. **초기화**: 애플리케이션 시작 시 `@PostConstruct`를 통해 스케줄러 시작
2. **스케줄 갱신**: 1분마다 `@Scheduled` 메서드를 통해 스케줄 정보 갱신
3. **Job 및 Trigger 관리**:
   - 기존 Job 확인 및 업데이트
   - 필요 시 신규 Job 등록
   - Trigger 상태 관리 (활성화/비활성화)
   - Cron 표현식 변경 시 재스케줄링
4. **Job 실행**:
   - 설정된 시간에 트리거 발동
   - Job 클래스 인스턴스화 및 `execute()` 메서드 호출
   - JobDataMap을 통해 필요한 데이터 전달

이 과정을 통해 Quartz 스케줄러는 설정된 시간에 맞춰 Job을 실행하고, 1분마다 스케줄 정보를 갱신하여 변경사항을 반영합니다.