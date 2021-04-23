// IServiceInterface.aidl
package com.example.test;
import com.example.test.data.BookInfo;
import com.example.test.IServiceCallback;

// Declare any non-default types here with import statements

interface IServiceInterface {
   BookInfo getBookInfo();

   void registListener(IServiceCallback listener);

   void unregistListener(IServiceCallback listener);
}