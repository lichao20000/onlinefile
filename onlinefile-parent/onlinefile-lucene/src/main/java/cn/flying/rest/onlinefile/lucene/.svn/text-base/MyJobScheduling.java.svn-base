package cn.flying.rest.onlinefile.lucene;

import java.util.Date;

import javax.annotation.Resource;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Component;
@Component
public class MyJobScheduling {
  
  @Resource(name = "scheduler")
  private Scheduler scheduler;
  
  public Scheduler getScheduler() {
    return scheduler;
  }
  
  public void setScheduler(Scheduler scheduler) {
    this.scheduler = scheduler;
    System.out.println(scheduler);
  }

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    try {
      Scheduler scheduler = new StdSchedulerFactory().getScheduler();
    
    scheduler.start();  

    JobDetail jobDetail = new JobDetail("helloWorldJob",  
            Scheduler.DEFAULT_GROUP, LuceneJob.class);  
    Trigger trigger = new SimpleTrigger("simpleTrigger",  
            Scheduler.DEFAULT_GROUP, new Date(), null,  
            SimpleTrigger.REPEAT_INDEFINITELY, 1000);  
      
     scheduler.scheduleJob(jobDetail, trigger);  
    
    } catch (SchedulerException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }  
  }

   public void addTrigger(){
    try {
      JobDetail jobDetail = new JobDetail();
      jobDetail.setName("helloWorldJob");//任务名称
      jobDetail.setGroup(Scheduler.DEFAULT_GROUP);//任务组名
      jobDetail.setJobClass(Class.forName("cn.flying.rest.onlinefile.lucene.LuceneJob"));//调用的任务类
    
      SimpleTrigger trigger = new SimpleTrigger("simpleTrigger",Scheduler.DEFAULT_GROUP);
     trigger.setStartTime(new Date());//开始时间
     trigger.setEndTime(null);//结束时间
     trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);//重复次数
     trigger.setRepeatInterval(1000);//执行间隔
     System.out.println(scheduler);
     this.scheduler.scheduleJob(jobDetail, trigger);
    } catch (Exception e) {
      e.printStackTrace();
    }  
   }
}
