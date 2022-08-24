This lib introduces class for representing result of calculation as value or exception.

### Example of usage

```java

import com.nutrymaco.result.Result;

import java.util.UUID;

class Program {

    public static void main(String[] args) {
        var user = new User("Alex", 21);
        var savingResult = saveUser(user);
        if (savingResult.isException()) {
            switch (savingResult.exception().source()) {
                case USER_VALIDATION -> //...;
                case QUERY_BUILDING -> //...;
                case DATABASE_QUERY -> //...;
            }
        }
    }
    
    static Result<Void, ExceptionSource> saveUser(User user) {
        var validationResult = validateUser(user);
        if (validationResult.isException()) {
            return validationResult;
        }
        var queryResult = buildQuery(user);
        if (queryResult.isException()) {
            return queryResult.toVoid();
        }
        var dbQueryResult = Result.create(() -> db.doQuery(query), DATABASE_QUERY);
        return dbQueryResult.toVoid();
    }

    static Result<Void, ExceptionSource> validateUser(User user) {
        if (user.age() > 0) {
            return Result.value(null);
        } else {
            return Result.exception(new RuntimeException("not valid user"), USER_VALIDATION);
        }
    }

    static Result<String, ExceptionSource> buildQuery(User user) {
        try {
            String query = QueryBuilder.create()
                    .insertTo("users")
                    .values(
                            "name", user.name(),
                            "age", user.age()
                    )
                    .build();
            return Result.value(query);
        } catch (Exception e) {
            return Result.exception(e, QUERY_BUILDING);
        }
    }

    static enum ExceptionSource {
        USER_VALIDATION,
        QUERY_BUILDING,
        DATABASE_QUERY
    }

    static record User(String name, int age) {}

}

```
