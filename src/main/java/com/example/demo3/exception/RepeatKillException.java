package com.example.demo3.exception;

/**
 * 重复执行秒杀的异常（运行期异常）

 */
public class RepeatKillException extends SeckillException {

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
