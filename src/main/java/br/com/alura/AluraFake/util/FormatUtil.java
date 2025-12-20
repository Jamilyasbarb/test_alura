package br.com.alura.AluraFake.util;

import br.com.alura.AluraFake.domain.enums.EnumDTOInterface;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FormatUtil {
    public static <E extends Enum<E> & EnumDTOInterface> List<Integer> getAllCodesFromEnum(Class<E> enumClass) {
        return Arrays.stream(enumClass.getEnumConstants())
                .map(EnumDTOInterface::getCode)
                .collect(Collectors.toList());
    }
}
