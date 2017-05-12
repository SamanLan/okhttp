/*
 * Copyright (C) 2014 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package okhttp3;

import java.io.IOException;

/**
 * 调用请求,准备执行。一个call可以取消。这个对象表示一个单一的请求/响应对(流),它不能被执行两次。
 */
public interface Call extends Cloneable {
  /** Returns the original request that initiated this call. */
  Request request();

  /**
   * 立即调用请求,直到可以处理或在错误的响应
   *
   * 为了避免泄漏资源调用者应该关闭Response，进而将关闭底层.
   *
   * 调用者可能会读取response body。为了避免泄漏资源调用者必须关闭response body或Response。
   *
   * <p>Note that transport-layer success (receiving a HTTP response code, headers and body) does
   * not necessarily indicate application-layer success: {@code response} may still indicate an
   * unhappy HTTP response code like 404 or 500.
   *
   * @throws IOException if the request could not be executed due to cancellation, a connectivity
   * problem or timeout. Because networks can fail during an exchange, it is possible that the
   * remote server accepted the request before the failure.
   * @throws IllegalStateException 当调用已经执行
   */
  Response execute() throws IOException;

  /**
   * Schedules the request to be executed at some point in the future.
   * 请求将会执行，除非有几个请求正在执行
   *
   * client将会通过回调返回response或者失败的exception
   *
   * @throws IllegalStateException when the call has already been executed.
   */
  void enqueue(Callback responseCallback);

  /** 取消请求，不是一定能取消，假如请求已完成就不能取消了 */
  void cancel();

  /**
   * 当call调用过了execute或者enqueue就返回true，记住call只能调用一次上述方法，否则报错
   */
  boolean isExecuted();

  boolean isCanceled();

  /**
   * 创建一个全新的完全相同的call，可以执行execute或者enqueue，即使原来的call已经执行过了这两个方法
   */
  Call clone();

  interface Factory {
    Call newCall(Request request);
  }
}
