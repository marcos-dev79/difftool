package com.curbee.chalenge;


import com.curbee.chalenge.domain.ChangeType;
import com.curbee.chalenge.testObj.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("diff-rest")
public class Controller {

    @GetMapping(value = "/diff", produces = "application/json")
    public @ResponseBody
    List<ChangeType> showDiff() {

        List<ChangeType> changes = new ArrayList<>();

        User previous = new User("1", "John", "john@test.com");
        User current = new User("1", "John", "john2@test.com");

        User one = new User("2", "Marcos", "myemail@gemail.com");
        User two = new User("3", "Lucas", "lucas@gemail.com");
        User three = new User("4", "Robert", "robert@gemail.com");
        User four = new User("5", "Pedro", "pedro@gemail.com");

        List<User> previousList = List.of(one, two);
        List<User> currentList = List.of(one, two, three, four);

        List<User> previousCompList = List.of(previous, two);
        List<User> currentCompList = List.of(current, two);

        DiffTool diffTool = new DiffTool();

        changes = diffTool.diff(previous, current);
        changes.addAll(diffTool.diff(previousList, currentList));
        changes.addAll(diffTool.diff(previousCompList, currentCompList));

        return changes;
    }

}
