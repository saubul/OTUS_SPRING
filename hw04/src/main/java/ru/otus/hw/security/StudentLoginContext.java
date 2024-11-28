package ru.otus.hw.security;

import ru.otus.hw.domain.Student;

public interface StudentLoginContext {

    void login(Student student);

    boolean isStudentLoggedIn();

    Student getCurrentStudent();

}
