package com.sjsu.cmpe226.mesonet.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

  private static final SessionFactory sessionFactoryMeta  = buildSessionFactoryMetaData();
  private static final SessionFactory sessionFactoryData = buildSessionFactoryData();

  //    private static final SessionFactory sessionFactoryContent = buildSessionFactoryContent();

  private static SessionFactory buildSessionFactoryMetaData() {
    try {
      return new Configuration()
      .configure("hibernate.cfg.xml")
      .addResource("com/sjsu/cmpe226/mesonet/vo/WeatherMetaDataVO.hbm.xml")
      .buildSessionFactory();

    } catch (Throwable ex) {
      System.err.println("Initial SessionFactory creation failed." + ex);
      throw new ExceptionInInitializerError(ex);
    }
  }

  private static SessionFactory buildSessionFactoryData() {
    try {
      return new Configuration().configure("hibernate.cfg.xml").addResource("com/sjsu/cmpe226/mesonet/vo/WeatherDataVO.hbm.xml").buildSessionFactory();
    } catch (Throwable ex) {
      System.err.println("Initial SessionFactory creation failed." + ex);
      throw new ExceptionInInitializerError(ex);
    }
  }

  //    private static SessionFactory buildSessionFactoryContent() {
  //        try {
  //            return new Configuration().configure("hibernate.cfg.xml").addResource("com/leantaas/community/vo/EightBallDataContentVO.hbm.xml").buildSessionFactory();
  //        } catch (Throwable ex) {
  //            System.err.println("Initial SessionFactory creation failed." + ex);
  //            throw new ExceptionInInitializerError(ex);
  //        }
  //    }

  public static SessionFactory getMetaSessionFactory() {
    return sessionFactoryMeta;
  }

  public static void shutdownHeader() {
    getMetaSessionFactory().close();
  }
  public static SessionFactory getDataSessionFactory() {
    return sessionFactoryData;
  }
  public static void shutdownData() {
    getDataSessionFactory().close();
  }

  //    public static SessionFactory getContentSessionFactory() {
  //        return sessionFactoryContent;
  //    }

  //    public static void shutdownContent() {
  //        getContentSessionFactory().close();
  //    }

}
