package br.com.alura.AluraFake.domain.enums;

public enum TaskType implements EnumDTOInterface {
    OPEN_TEXT(1, "Cursive Answer"),
    MULTIPLE_CHOICE(2, "Multiple Choice"),
    SINGLE_CHOICE(3, "Single Choice");

    private int code;
    private String description;

    TaskType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static TaskType toEnum(Integer code) {
        if (code == null) {
            return null;
        }

        for (TaskType x : TaskType.values()) {
            if (code.equals(x.getCode())) {
                return x;
            }
        }

        throw new IllegalArgumentException("Código Inválido: " + code);
    }
}
