package core.basesyntax.service.readservice;

import core.basesyntax.model.FruitTransaction;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TransactionParseServiceImpl implements TransactionParseService {
    private static final int FIRST_INDEX = 0;
    private static final int SECOND_INDEX = 1;
    private static final int THIRD_INDEX = 2;
    private static final String SEPARATOR = ",";
    private static final String FIRST_COLUMN = "type";
    private static final String SECOND_COLUMN = "fruits";
    private static final String THIRD_COLUMN = "quantity";
    private static final String REGEX = "[0-9]+";

    @Override
    public List<FruitTransaction> parse(String dataFromCsvFile) {
        if (dataFromCsvFile == null) {
            throw new RuntimeException("Data from file is null");
        }
        List<String> data = new ArrayList<>(List.of(dataFromCsvFile.split(System.lineSeparator())));
        if (!isColumnsValid(data.get(0))) {
            throw new RuntimeException("Incorrect format of data");
        }
        return data.stream()
                .skip(1)
                .map(element -> {
                    if (!isRowValid(element)) {
                        throw new RuntimeException("Incorrect row format " + element);
                    }
                    String[] separateElements = element.split(SEPARATOR);
                    String operation = separateElements[FIRST_INDEX];
                    String fruit = separateElements[SECOND_INDEX];
                    int quantity = Integer.parseInt(separateElements[THIRD_INDEX]);
                    return new FruitTransaction(operation, fruit, quantity);
                })
                .collect(Collectors.toList());
    }

    private boolean isColumnsValid(String columns) {
        String[] columnsSplit = columns.split(SEPARATOR);
        return columnsSplit.length == 3
                && Objects.equals(columnsSplit[FIRST_INDEX], FIRST_COLUMN)
                && Objects.equals(columnsSplit[SECOND_INDEX], SECOND_COLUMN)
                && Objects.equals(columnsSplit[THIRD_INDEX], THIRD_COLUMN);
    }

    private boolean isRowValid(String row) {
        String[] rowSplit = row.split(SEPARATOR);
        return (rowSplit.length == 3)
                && rowSplit[2].matches(REGEX);
    }
}
