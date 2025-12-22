package br.com.alura.AluraFake.domain.enums;

public enum Role {
    STUDENT,
    INSTRUCTOR;

    public static Role toEnumFromName(String name) {
        if (name == null) {
            return null;
        }

        for (Role x : Role.values()) {
            if (name.equals(x.name())) {
                return x;
            }
        }

        throw new IllegalArgumentException("Invalid name: " + name);
    }
}
