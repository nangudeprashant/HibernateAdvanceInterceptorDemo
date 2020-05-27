package com.javalive.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.javalive.entity.Book;
import com.javalive.interceptor.LoggingInterceptor;


public class MainApp {

   public static void main(String[] args) {

      Session session = null;
      Transaction transaction = null;
      try {
         session = HibernateUtil.getSessionFactory()
               .withOptions()
               .interceptor(new LoggingInterceptor()) // add interceptor to Session
               .openSession();
         
         transaction = session.getTransaction();
         transaction.begin();
         
         Book book=new Book();
         book.setTitle("Java 8: A Beginner's Guide");
         book.setAuthor("Herbert Schildt");
         session.save(book);
         
         transaction.commit();
      } catch (Exception e) {
         if (transaction != null) {
            transaction.rollback();
         }
         e.printStackTrace();
      } finally {
         if (session != null) {
            session.close();
         }
      }
      HibernateUtil.shutdown();
   }

}
