package br.com.alura.AluraFake.exception;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String message) {
        super(message);
    }

    public ObjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectNotFoundException(Class<?> object) {
        super(object.getSimpleName() + " não encontrado! Tipo: " + object.getName());
    }
}
