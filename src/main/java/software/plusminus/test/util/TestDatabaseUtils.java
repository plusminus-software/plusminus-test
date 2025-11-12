package software.plusminus.test.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestDatabaseUtils {

    public String updateQueryWithParameters(String originalQuery, String... parameters) {
        long questionMarksCount = originalQuery.chars()
                .filter(c -> c == '?')
                .count();
        if (parameters.length != questionMarksCount) {
            throw new IllegalArgumentException("Cannot update query: the query expected "
                    + questionMarksCount + " parameters where there are " + parameters.length + " provided");
        }
        String updatedQuery = originalQuery;
        for (Object parameter : parameters) {
            updatedQuery = updatedQuery.replaceFirst("\\?", parameter.toString());
        }
        return updatedQuery;
    }
}
