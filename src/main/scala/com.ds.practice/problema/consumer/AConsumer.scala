package com.ds.practice.problema.consumer

import java.util.concurrent.BlockingQueue


/**
 * Common code for required by all consumers.
 * It should be a thread and its run method implementation for continuous consumption from queue.
 * @param queue from where the consumer will consume its tasks
 * @tparam T
 */
abstract class AConsumer[T](queue: BlockingQueue[T]) extends Runnable {

  def run() {
    while (true) {
      val item = queue.take()
      consume(item)
    }
  }

  def consume(x: T)
}

