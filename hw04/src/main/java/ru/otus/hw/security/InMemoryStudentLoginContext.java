package ru.otus.hw.security;

import org.springframework.stereotype.Component;
import ru.otus.hw.domain.Student;

import java.util.Objects;

@Component
public class InMemoryStudentLoginContext implements StudentLoginContext {

    private Student student;

    @Override
    public void login(Student student) {
        this.student = student;
    }

    @Override
    public boolean isStudentLoggedIn() {
        return Objects.nonNull(student);
    }

    @Override
    public Student getCurrentStudent() {
        return this.student;
    }

}
