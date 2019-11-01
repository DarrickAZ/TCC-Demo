package com.luke.milo.common.exception;

/**
 * @Descrtption MiloException
 * @Author luke
 * @Date 2019/9/24
 **/
@SuppressWarnings("all")
public class MiloException extends RuntimeException{

    private static final long serialVersionUID = 4758435008817435088L;

    public MiloException() {
    }

    public MiloException(String message) {
        super(message);
    }

    public MiloException(String message, Throwable cause) {
        super(message, cause);
    }

    public MiloException(Throwable cause) {
        super(cause);
    }

}
