package it.spaceschool.ui;

import it.spaceschool.model.Course;
import it.spaceschool.model.Student;
import it.spaceschool.service.CourseService;
import it.spaceschool.service.StudentService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class ProfileView {

    private final AppShell shell;
    private final CourseService courseService;
    private final StudentService studentService;
    private final Student logged;

    private final VBox root = new VBox(16);

    public ProfileView(AppShell shell, CourseService courseService, StudentService studentService, Student logged) {
        this.shell = shell;
        this.courseService = courseService;
        this.studentService = studentService;
        this.logged = logged;

        Label title = new Label("Profilo");
        title.getStyleClass().add("title");

        // --- پیام notification ---

        Label msg = new Label();
        msg.getStyleClass().add("notify-badge");
        msg.setVisible(false);   // 👈 اول مخفی


        // --- کارت اطلاعات دانشجو (سمت چپ) ---
        VBox leftCard = new VBox(10);
        leftCard.getStyleClass().add("glass-card");
        leftCard.setPadding(new Insets(18));
        leftCard.setPrefWidth(380);

        Label lblWelcome = new Label("Benvenuta " + logged.getFullName());
        lblWelcome.getStyleClass().add("profile-text");

        Label lblEmail = new Label("Email: " + logged.getEmail());
        lblEmail.getStyleClass().add("profile-text");

        Label lblBirth = new Label("Birth date: " + logged.getBirthDate());
        lblBirth.getStyleClass().add("profile-text");

        Label lblMotivation = new Label("Motivation letter: " + logged.getMotivationLetterPath());
        lblMotivation.getStyleClass().add("profile-text");

        leftCard.getChildren().addAll(lblWelcome, lblEmail, lblBirth, lblMotivation);

        // --- لیست کورس‌های قابل ثبت‌نام (سمت راست) ---
        VBox rightCard = new VBox(10);
        rightCard.getStyleClass().add("glass-card");
        rightCard.setPadding(new Insets(18));
        rightCard.setPrefWidth(420);

        Label coursesTitle = new Label("Corsi disponibili");
        coursesTitle.getStyleClass().add("title");

        VBox courseList = new VBox(10);

        List<Course> courses = courseService.getAllCourses();
        for (Course c : courses) {

            // ردیف هر کورس
            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);

            Label courseLabel = new Label(
                    c.getName() + " (" + c.getLevel() + ", " + c.getDurationHours() + "h)"
            );
            courseLabel.getStyleClass().add("profile-text");

            Hyperlink enrollLink = new Hyperlink("Iscriviti");
            enrollLink.getStyleClass().add("link");

            // اگر قبلاً enroll شده باشد، لینک غیرفعال شود
            boolean already = logged.getEnrolledCourseIds().contains(c.getId());
            if (already) {
                enrollLink.setDisable(true);
                enrollLink.setText("Iscritto ✅");
            }

            enrollLink.setOnAction(e -> {
                try {
                    // enroll
                    studentService.enrollCourse(logged, c.getId());

                    // UI update
                    enrollLink.setDisable(true);
                    enrollLink.setText("Iscritto ✅");

                    // notification (ایمیل واقعی نیست)
                    msg.setText("✅ Iscrizione completata! Il link della lezione e altre dettaghi sul corso è stato inviato alla tua email: " + logged.getEmail());
                    msg.setVisible(true);
                } catch (Exception ex) {
                    msg.setText("❌ " + ex.getMessage());

                }
            });

            row.getChildren().addAll(courseLabel, enrollLink);
            courseList.getChildren().add(row);
        }

        rightCard.getChildren().addAll(coursesTitle, courseList);

        // --- logout ---
        Button btnLogout = new Button("Logout");
        btnLogout.getStyleClass().add("primary-btn");
        btnLogout.setPrefWidth(240);
        btnLogout.setOnAction(e ->
                shell.setContent(new HomeView(shell, courseService, studentService).getRoot())
        );

        // --- چیدمان کلی ---
        HBox content = new HBox(18, leftCard, rightCard);
        content.setAlignment(Pos.CENTER);

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(18));
        root.getChildren().addAll(title, content, msg, btnLogout);
    }

    public Parent getRoot() {
        return root;
    }
}
