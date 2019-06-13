package calendar.ui.main;

import calendar.data.model.MyCalendar;
import calendar.database.DBHandler;
import calendar.ui.addcalendar.AddCalendarController;
import calendar.ui.addevent.AddEventController;
import calendar.ui.editevent.EditEventController;
import calendar.ui.listcalendars.ListCalendarsController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXColorPicker;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.effects.JFXDepthManager;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FXMLDocumentController implements Initializable {

    //Date and time fields, Calendar Label
    @FXML
    private JFXDatePicker selectedDate;
    @FXML
    private Label thisTime;
    @FXML
    private Label thisDate;
    @FXML
    private Label calendarNameLbl;
    @FXML
    private Label nameOfChoosenCalendar;
    @FXML
    private JFXTabPane tabPane;

    //DAY
    @FXML
    private Tab dayPan;
    @FXML
    private Label dayLbl;
    @FXML
    private HBox dayHeaderForDay;
    @FXML
    private Label headersDayLbl;
    @FXML
    private GridPane dayView1;
    @FXML
    private GridPane dayView2;
    @FXML
    private GridPane dayView3;
    @FXML
    private JFXButton buttonPrevDay;
    @FXML
    private JFXButton buttonNextDay;


    //WEEK
    @FXML
    private VBox vBoxWeek;
    @FXML
    private HBox weekInfo;
    @FXML
    private Label weekLbl;
    @FXML
    private ScrollPane scrollPaneWeek;
    @FXML
    private JFXButton buttonPrevWeek;
    @FXML
    private JFXButton buttonNextWeek;
    @FXML
    private HBox weekdayHeaderForWeek;
    @FXML
    private HBox weekdayHeaderForWeek1;
    @FXML
    private GridPane weekView;
    @FXML
    private GridPane weekView1;
    @FXML
    private GridPane weekView2;
    @FXML
    private GridPane weekView3;
    @FXML
    private GridPane weekView4;
    @FXML
    private GridPane weekView5;
    @FXML
    private GridPane weekView6;
    @FXML
    private Label labelDivider;
    @FXML
    private Label labelDivider1;
    @FXML
    private Label labelDivider2;
    @FXML
    private Label labelDivider3;
    @FXML
    private Label labelDivider4;


    //MONTH
    @FXML
    private Label monthLbl;
    @FXML
    private HBox weekdayHeader;
    @FXML
    private Tab monthPan;
    @FXML
    private JFXButton buttonPrevMonth;
    @FXML
    private JFXButton buttonNextMonth;
    @FXML
    private ScrollPane scrollPaneMonth;
    @FXML
    private GridPane monthView;


    // YEAR
    @FXML
    private HBox weekdayHeaderForYear;
    @FXML
    private Label yearLbl;
    @FXML
    private Label headersYearLbl;
    @FXML
    private GridPane yearView;
    @FXML
    private JFXButton buttonPrevYear;
    @FXML
    private JFXButton buttonNextYear;


    //--------- Database Handler -----------------------------------------
    DBHandler databaseHandler;
    //--------------------------------------------------------------------

    // Color pickers
    @FXML
    private JFXColorPicker workCP;
    @FXML
    private JFXColorPicker studyCP;
    @FXML
    private JFXColorPicker sportCP;
    @FXML
    private JFXColorPicker vacationCP;
    @FXML
    private JFXColorPicker birthdaysCP;
    @FXML
    private JFXColorPicker holidaysCP;
    @FXML
    private JFXColorPicker otherCP;


    //**************************************************************************
    //**************************************************************************
    //**************************************************************************

    // Events
    private void addEvent(Pane day) {

        // Purpose - Add event to a day
        if (MyCalendar.getInstance().calendar_name != null) {

            // Do not add events to days without labels
            if (!day.getChildren().isEmpty()) {

                // Get the day number
                Label lbl = (Label) day.getChildren().get(0);

                if (lbl.getText().contains(":")) {
                    MyCalendar.getInstance().event_day = selectedDate.getValue().getDayOfMonth();
                } else {
                    // Store event day and month in data singleton
                    MyCalendar.getInstance().event_day = Integer.parseInt(lbl.getText());
                }
                MyCalendar.getInstance().event_month = selectedDate.getValue().getMonthValue();
                MyCalendar.getInstance().event_year = selectedDate.getValue().getYear();


                // Open add event view
                try {
                    // Load root layout from fxml file.
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/calendar/ui/addevent/add_event.fxml"));
                    AnchorPane rootLayout = (AnchorPane) loader.load();
                    Stage stage = new Stage(StageStyle.UNDECORATED);
                    stage.initModality(Modality.APPLICATION_MODAL);

                    // Pass main controller reference to view
                    AddEventController eventController = loader.getController();
                    eventController.setMainController(this);

                    // Show the scene containing the root layout.
                    Scene scene = new Scene(rootLayout);
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void editEvent(Pane day, String descript, String categorieID) {

        // Store event fields in data singleton
        Label dayLbl = (Label) day.getChildren().get(0);
        if (dayLbl.getText().contains(":")) {
            MyCalendar.getInstance().event_day = selectedDate.getValue().getDayOfMonth();
        } else {
            MyCalendar.getInstance().event_day = Integer.parseInt(dayLbl.getText());
        }
        MyCalendar.getInstance().event_subject = descript;
        MyCalendar.getInstance().event_categorie = Integer.parseInt(categorieID);


        // When user clicks on any date in the calendar, event editor window opens
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/calendar/ui/editevent/edit_event.fxml"));
            AnchorPane rootLayout = (AnchorPane) loader.load();
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);

            // Pass main controller reference to view
            EditEventController eventController = loader.getController();
            eventController.setMainController(this);

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void newCalendarEvent() {
        // New Calendar view
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/calendar/ui/addcalendar/add_calendar.fxml"));
            AnchorPane rootLayout = (AnchorPane) loader.load();
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);

            // Pass main controller reference to view
            AddCalendarController calendarController = loader.getController();
            calendarController.setMainController(this);

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void listCalendarsEvent() {
        // List Calendar view
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/calendar/ui/listcalendars/list_calendars.fxml"));
            AnchorPane rootLayout = (AnchorPane) loader.load();
            Stage stage = new Stage(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);

            // Pass main controller reference to view
            ListCalendarsController listController = loader.getController();
            listController.setMainController(this);

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initializeDateSelector() {

        //DataPicker
        selectedDate.setConverter(new StringConverter<LocalDate>() {
            String pattern = "dd-MM-YYYY";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            {
                selectedDate.setPromptText(pattern.toLowerCase());
            }

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });


        // Add event listener to each month list item, allowing user to change date
        selectedDate.valueProperty().addListener((observable, oldValue, newValue) -> {


                    // Necessary to check for null because change listener will
                    // also detect clear() calls
                    if (newValue != null) {

                        // Update the VIEWING MONTH
                        MyCalendar.getInstance().viewing_month = newValue.getMonthValue();

                        MyCalendar.getInstance().viewing_year = newValue.getYear();
                        MyCalendar.getInstance().viewing_day = newValue.getDayOfYear();
                        MyCalendar.getInstance().viewing_week = newValue.getDayOfWeek().getValue();
                        MyCalendar.getInstance().viewing_day_of_month = newValue.getDayOfMonth();

                        // Update view
                        repaintView();
                    }
                }
        );

    }

    private void loadCalendarLabels() {
        loadYearLabels();
        loadMonthLabels();
        loadWeekLabels();
        loadDayLabels();
    }

    private void loadYearLabels() {
        yearLbl.setText(" " + MyCalendar.getInstance().viewing_year);
    }

    private void loadDayLabels() {

        GridPane[] dayHours = new GridPane[]{dayView1, dayView2, dayView3};

        // Go through day calendar grids
        for (int i = 0; i < dayHours.length; i++) {

            for (Node node : dayHours[i].getChildren()) {
                VBox dayPart = (VBox) node;
                dayPart.getChildren().clear();
                dayPart.setStyle("-fx-backgroud-color: white");
                dayPart.setStyle("-fx-font: 14px \"System\" ");

                Label lbl = new Label(" " + i * 8 + ":00 - " + (i + 1) * 8 + ":00  ");
                lbl.setPadding(new Insets(5));
                lbl.setStyle("-fx-text-fill:darkslategray");
                dayPart.getChildren().add(lbl);
            }
        }
    }

    private void loadMonthLabels() {

        // Get the current VIEW
        int year = MyCalendar.getInstance().viewing_year;
        int month = MyCalendar.getInstance().viewing_month;

        monthLbl.setText(MyCalendar.getInstance().getMonth(month) + "  " + year);

        LocalDate gc = LocalDate.of(MyCalendar.getInstance().viewing_year, MyCalendar.getInstance().viewing_month, 1);
        int daysInMonth = gc.lengthOfMonth();
        int firstDay = gc.getDayOfWeek().getValue();

        int offset = firstDay;
        int gridCount = 1;
        int lblCount = 1;

        // Go through calendar grid
        for (Node node : monthView.getChildren()) {

            VBox dayOfMonth = (VBox) node;

            dayOfMonth.getChildren().clear();
            dayOfMonth.setStyle("-fx-backgroud-color: white");
            dayOfMonth.setStyle("-fx-font: 14px \"System\" ");

            // Start placing labels on the first dayOfMonth for the month
            if (gridCount < offset) {
                gridCount++;
                // Darken color of the offset days
                dayOfMonth.setStyle("-fx-background-color: #e9e7ea");
            } else {

                // Don't place a label if we've reached maximum label for the month
                if (lblCount > daysInMonth) {
                    // Instead, darken dayOfMonth color
                    dayOfMonth.setStyle("-fx-background-color: #e9e7ea");
                } else {

                    // Make a new dayOfMonth label
                    Label lbl = new Label(Integer.toString(lblCount));
                    lbl.setPadding(new Insets(5));
                    lbl.setStyle("-fx-text-fill:darkslategray");

                    dayOfMonth.getChildren().add(lbl);
                }
                lblCount++;
            }
        }
    }

    //WEEK
    private void loadWeekLabels() {

        // Get the current VIEW
        LocalDate[] allDaysOfSelectedWeek = MyCalendar.getInstance().getAllDaysOfSelectedWeek();
        GridPane[] weekViewGridForDay = new GridPane[]{weekView, weekView1, weekView2, weekView3, weekView4, weekView5, weekView6};

        weekLbl.setText("FROM  " + allDaysOfSelectedWeek[0].getDayOfMonth() + " " + allDaysOfSelectedWeek[0].getMonth()
                + " " + allDaysOfSelectedWeek[0].getYear() + "  TO  " + allDaysOfSelectedWeek[6].getDayOfMonth()
                + " " + allDaysOfSelectedWeek[6].getMonth() + " " + allDaysOfSelectedWeek[6].getYear());

        // Go through calendar grid
        for (int i = 0; i < weekViewGridForDay.length; i++) {

            for (Node node : weekViewGridForDay[i].getChildren()) {
                VBox dayOfWeek = (VBox) node;
                dayOfWeek.getChildren().clear();
                dayOfWeek.setStyle("-fx-backgroud-color: white");
                dayOfWeek.setStyle("-fx-font: 14px \"System\" ");

                Label lbl = new Label(Integer.toString(allDaysOfSelectedWeek[i].getDayOfMonth()));
                lbl.setPadding(new Insets(5));
                lbl.setStyle("-fx-text-fill:darkslategray");
                dayOfWeek.getChildren().add(lbl);
            }
        }
    }

    public void calendarGenerate() {

        // Set calendar name label
        calendarNameLbl.setText(MyCalendar.getInstance().calendar_name);
        selectedDate.setValue(LocalDate.now());

        // Load year and month selection
        MyCalendar.getInstance().viewing_year = selectedDate.getValue().getYear();
        MyCalendar.getInstance().viewing_month = selectedDate.getValue().getMonthValue();
        MyCalendar.getInstance().viewing_day = selectedDate.getValue().getDayOfYear();
        MyCalendar.getInstance().viewing_week = selectedDate.getValue().getDayOfWeek().getValue();
        MyCalendar.getInstance().viewing_day_of_month = selectedDate.getValue().getDayOfMonth();

        // Update view
        initializeButtonPrevNext();
    }


    public void repaintView() {
        //Display events known to database

        loadCalendarLabels();

        headersDayLbl.setText(selectedDate.getValue().getDayOfWeek().toString() + ", " + selectedDate.getValue().getDayOfMonth()
                + " " + selectedDate.getValue().getMonth() + " " + selectedDate.getValue().getYear());
        headersYearLbl.setText("" + selectedDate.getValue().getYear());
        populateMonthWithEvents();
        populateWeekWithEvents();
        populateDayWithEvents();
    }

    //WEEK
    private void populateWeekWithEvents() {

        String calendarName = MyCalendar.getInstance().calendar_name;
        GridPane[] weekViewGridForDay = new GridPane[]{weekView, weekView1, weekView2, weekView3, weekView4, weekView5, weekView6};
        LocalDate[] currentWeek = MyCalendar.getInstance().getAllDaysOfSelectedWeek();

        // Query to get ALL Events from the selected calendar!!
        String getAllEventsQuery = "SELECT * From EVENTS WHERE CalendarName='" + calendarName + "'";

        // Store the results here
        ResultSet result = databaseHandler.executeQuery(getAllEventsQuery);

        try {
            while (result.next()) {

                // Get date for the event
                Date startDate = result.getDate("EventStartDate");
                Date endDate = result.getDate("EventEndDate");
                String startTime = result.getTime("EventStartTime").toString().substring(0, 5);
                String endTime = result.getTime("EventEndTime").toString().substring(0, 5);
                String eventDescript = result.getString("EventDescription");
                int eventCategorieID = result.getInt("CategorieID");

                String eventTime = startTime + " - " + endTime;

                for (int i = 0; i < weekViewGridForDay.length; i++) {

                    if (startDate.toLocalDate().isEqual(currentWeek[i]) || endDate.toLocalDate().isEqual(currentWeek[i]) ||
                            (startDate.toLocalDate().isBefore(currentWeek[i]) && endDate.toLocalDate().isAfter(currentWeek[i]))) {

                        // Get day for the Days of week
                        int startDay = startDate.toLocalDate().getDayOfMonth();
                        int endDay = endDate.toLocalDate().getDayOfMonth();
                        showEveryDate(weekViewGridForDay[i], startDay, endDay, eventTime, eventDescript, eventCategorieID);
                    }
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(AddEventController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //DAY
    private void populateDayWithEvents() {
        // Get viewing calendar
        String calendarName = MyCalendar.getInstance().calendar_name;
        int currentMonthIndex = MyCalendar.getInstance().viewing_month;
        int currentYear = MyCalendar.getInstance().viewing_year;

        // Query to get ALL Events from the selected calendar!!
        String getDayEventsQuery = "SELECT * From EVENTS WHERE CalendarName='" + calendarName + "'";

        // Store the results here
        ResultSet result = databaseHandler.executeQuery(getDayEventsQuery);

        try {
            while (result.next()) {

                // Get date for the event
                Date startDate = result.getDate("EventStartDate");
                Date endDate = result.getDate("EventEndDate");
                String eventDescript = result.getString("EventDescription");
                int eventCategorieID = result.getInt("CategorieID");
                String comment = result.getString("EventComment");
                //long eventDuration = between(LocalTime.of(0, 0, 0), result.getTime("EventStartTime").toLocalTime()).getSeconds();
                String eventTime = result.getTime("EventStartTime").toString().substring(0, 5)
                        + " - " + result.getTime("EventEndTime").toString().substring(0, 5);

                if (currentYear == startDate.toLocalDate().getYear()) {
                    // Check for the month we already have selected (we are viewing)
                    if (currentMonthIndex == startDate.toLocalDate().getMonthValue()) {

                        ArrayList<LocalDate> eventDates = new ArrayList<>();
                        for (int d = 0; d <= endDate.toLocalDate().getDayOfMonth() - startDate.toLocalDate().getDayOfMonth(); d++)
                            eventDates.add(LocalDate.of(startDate.toLocalDate().getYear(), startDate.toLocalDate().getMonthValue(), startDate.toLocalDate().getDayOfMonth() + d));

                        if (eventDates.contains(selectedDate.getValue())) {
                            // Display decription of the event given it's day
                            ArrayList<Integer> first = new ArrayList<>();
                            for (int i = 0; i < 8; i++) first.add(i);
                            ArrayList<Integer> second = new ArrayList<>();
                            for (int i = 8; i < 16; i++) second.add(i);
                            ArrayList<Integer> third = new ArrayList<>();
                            for (int i = 16; i < 24; i++) third.add(i);
                            int search = Integer.parseInt(eventTime.substring(0, 2));
                            // Did we find a match?
                            if (first.contains(search)) {
                                // Add label to calendar
                                showDayDate(dayView1, eventTime, eventDescript, comment, eventCategorieID);
                            } else if (second.contains(search)) {
                                // Add label to calendar
                                showDayDate(dayView2, eventTime, eventDescript, comment, eventCategorieID);
                            } else if (third.contains(search)) {
                                // Add label to calendar
                                showDayDate(dayView3, eventTime, eventDescript, comment, eventCategorieID);
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddEventController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void showDayDate(GridPane view, String eventTime, String descript, String comment, int categorieID) {

        Image img = new Image(getClass().getClassLoader().getResourceAsStream("calendar/ui/icons/icon2.png"));
        ImageView imgView = new ImageView();
        imgView.setImage(img);

        for (Node node : view.getChildren()) {

            // Get the current day
            VBox part = (VBox) node;

            // Don't look at any empty days (they at least must have a day label!)
            if (!part.getChildren().isEmpty()) {

                // Add an event label with the given description
                Label eventLbl = new Label(eventTime + "\n" + descript + "\n" + comment);
                //Label eventLbl = new Label(eventTime + "\n" + descript);
                eventLbl.setGraphic(imgView);
                eventLbl.getStyleClass().add("event-label");

                // Save the categorie ID in accessible text
                eventLbl.setAccessibleText(Integer.toString(categorieID));
                System.out.println(eventLbl.getAccessibleText());

                eventLbl.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
                    editEvent((VBox) eventLbl.getParent(), eventLbl.getText().substring(14, eventLbl.getText().substring(14).indexOf("\n")+14), eventLbl.getAccessibleText());

                });

                // Get categorie color from categorie's table
                String eventRGB = databaseHandler.getCategorieColor(categorieID);

                // Parse for rgb values
                String[] colors = eventRGB.split("-");
                String red = colors[0];
                String green = colors[1];
                String blue = colors[2];

                System.out.println("Color; " + red + green + blue);

                eventLbl.setStyle("-fx-background-color: rgb(" + red +
                        ", " + green + ", " + blue + ", " + 1 + ");");

                // Stretch to fill box
                eventLbl.setMaxWidth(Double.MAX_VALUE);

                // Mouse effects
                eventLbl.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> {
                    eventLbl.getScene().setCursor(Cursor.HAND);
                });

                eventLbl.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> {
                    eventLbl.getScene().setCursor(Cursor.DEFAULT);
                });

                part.getChildren().add(eventLbl);
            }
        }
    }


    private void populateMonthWithEvents() {

        // Get viewing calendar
        String calendarName = MyCalendar.getInstance().calendar_name;
        int currentMonthIndex = MyCalendar.getInstance().viewing_month;
        int currentYear = MyCalendar.getInstance().viewing_year;

        // Query to get ALL Events from the selected calendar!!
        String getMonthEventsQuery = "SELECT * From EVENTS WHERE CalendarName='" + calendarName + "'";

        // Store the results here
        ResultSet result = databaseHandler.executeQuery(getMonthEventsQuery);

        try {
            while (result.next()) {

                // Get date for the event
                Date startDate = result.getDate("EventStartDate");
                Date endDate = result.getDate("EventEndDate");
                String eventDescript = result.getString("EventDescription");
                int eventCategorieID = result.getInt("CategorieID");
                String eventTime = result.getTime("EventStartTime").toString().substring(0, 5)
                        + " - " + result.getTime("EventEndTime").toString().substring(0, 5);


                if (currentYear == startDate.toLocalDate().getYear()) {
                    // Check for the month we already have selected (we are viewing)
                    if (currentMonthIndex == startDate.toLocalDate().getMonthValue()) {

                        // Get days for the month
                        int startDay = startDate.toLocalDate().getDayOfMonth();
                        int endDay = endDate.toLocalDate().getDayOfMonth();

                        // Display decription of the event given it's day
                        showEveryDate(monthView, startDay, endDay, eventTime, eventDescript, eventCategorieID);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AddEventController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void showEveryDate(GridPane view, int startDay, int endDay, String eventTime, String eventDescript, int eventCategorieID) {
        for (int i = startDay; i <= endDay; i++) {
            showDate(view, i, eventTime, eventDescript, eventCategorieID);
        }
    }

    public void showDate(GridPane view, int dayNumber, String eventTime, String descript, int categorieID) {

        Image img = new Image(getClass().getClassLoader().getResourceAsStream("calendar/ui/icons/icon2.png"));
        ImageView imgView = new ImageView();
        imgView.setImage(img);

        for (Node node : view.getChildren()) {

            // Get the current day
            VBox day = (VBox) node;

            // Don't look at any empty days (they at least must have a day label!)
            if (!day.getChildren().isEmpty()) {

                // Get the day label for that day
                Label lbl = (Label) day.getChildren().get(0);

                // Get the number
                int currentNumber = Integer.parseInt(lbl.getText());

                // Did we find a match?
                if (currentNumber == dayNumber) {

                    // Add an event label with the given description
                    Label eventLbl = new Label(eventTime + "\n" + descript);
                    eventLbl.setGraphic(imgView);
                    eventLbl.getStyleClass().add("event-label");

                    // Save the categorie ID in accessible text
                    eventLbl.setAccessibleText(Integer.toString(categorieID));
                    System.out.println(eventLbl.getAccessibleText());

                    eventLbl.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
                        editEvent((VBox) eventLbl.getParent(), eventLbl.getText().substring(14), eventLbl.getAccessibleText());

                    });

                    // Get categorie color from categorie's table
                    String eventRGB = databaseHandler.getCategorieColor(categorieID);

                    // Parse for rgb values
                    String[] colors = eventRGB.split("-");
                    String red = colors[0];
                    String green = colors[1];
                    String blue = colors[2];

                    System.out.println("Color; " + red + green + blue);

                    eventLbl.setStyle("-fx-background-color: rgb(" + red +
                            ", " + green + ", " + blue + ", " + 1 + ");");

                    // Stretch to fill box
                    eventLbl.setMaxWidth(Double.MAX_VALUE);

                    // Mouse effects
                    eventLbl.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> {
                        eventLbl.getScene().setCursor(Cursor.HAND);
                    });

                    eventLbl.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> {
                        eventLbl.getScene().setCursor(Cursor.DEFAULT);
                    });

                    // Add label to calendar
                    day.getChildren().add(eventLbl);
                }
            }
        }
    }


/*    public void exportCalendarPDF() {
        TableView<MyEvent> table = new TableView<MyEvent>();
        ObservableList<MyEvent> data = FXCollections.observableArrayList();


        double w = 500.00;
        // set width of table view
        table.setPrefWidth(w);
        // set resize policy
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // intialize columns
        TableColumn<MyEvent, String> categorie = new TableColumn<MyEvent, String>("Categorie");
        TableColumn<MyEvent, String> subject = new TableColumn<MyEvent, String>("Subject");
        TableColumn<MyEvent, String> date = new TableColumn<MyEvent, String>("Date");
        // set width of columns
        categorie.setMaxWidth(1f * Integer.MAX_VALUE * 20); // 50% width
        subject.setMaxWidth(1f * Integer.MAX_VALUE * 60); // 50% width
        date.setMaxWidth(1f * Integer.MAX_VALUE * 20); // 50% width
        // 
        categorie.setCellValueFactory(new PropertyValueFactory<MyEvent, String>("categorie"));
        subject.setCellValueFactory(new PropertyValueFactory<MyEvent, String>("subject"));
        date.setCellValueFactory(new PropertyValueFactory<MyEvent, String>("date"));

        // Add columns to the table
        table.getColumns().add(categorie);
        table.getColumns().add(subject);
        table.getColumns().add(date);

        String calendarName = MyCalendar.getInstance().calendar_name;

        // Query to get ALL Events from the selected calendar!!
        String getMonthEventsQuery = "SELECT * From EVENTS WHERE CalendarName='" + calendarName + "' ORDER BY EventDate";

        // Store the results here
        ResultSet result = databaseHandler.executeQuery(getMonthEventsQuery);

        try {

            while (result.next()) {
                //initalize temporarily strings
                String evCategorie = "";


                //***** Get Categorie, Event Description and Date *****

                //Get Event Description
                String eventDescript = result.getString("EventDescription");
                //Get Categorie ID for an event
                int categorieID = result.getInt("CategorieID");

                //Get Event starting and ending date and format it as day-month-year
                Date stDate = result.getDate("EventStartDate");
                Date eDate = result.getDate("EventEndDate");
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String startDate = df.format(stDate);
                String endDate = df.format(stDate);

                Time stTime = result.getTime("EventStartTime");

                //Query that will get the categorie name based on a categorie ID
                String getCategorieQuery = "SELECT CategorieName FROM CATEGORIES WHERE CategorieID=" + categorieID + "";
                //Execute query to get CategorieName and store it in a ResultSet variable
                ResultSet termResult = databaseHandler.executeQuery(getCategorieQuery);


                while (termResult.next()) {
                    evCategorie = termResult.getString(1);
                     *//*
                      while(programResult.next())
                        {
                           tempProgram = programResult.getString(1);
                        }
                      tempTerm+=" "+tempProgram;
                    *//*
                }


                //Add event information in a row
                data.add(new MyEvent(eventDescript, evCategorie, startDate, endDate));

            }
        } catch (SQLException ex) {
            Logger.getLogger(AddEventController.class.getName()).log(Level.SEVERE, null, ex);
        }


        table.getItems().setAll(data);
        // open dialog window and export table as pdf
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            job.printPage(table);
            job.endJob();
        }
    }*/

    private String getRGB(Color c) {

        String rgb = Integer.toString((int) (c.getRed() * 255)) + "-"
                + Integer.toString((int) (c.getGreen() * 255)) + "-"
                + Integer.toString((int) (c.getBlue() * 255));

        return rgb;
    }

    private void changeColors() {

        // Purpose - Take all chosen colors in the Colors menu and
        // update each categorie's color in the database

        Color workColor = workCP.getValue();
        String workRGB = getRGB(workColor);
        databaseHandler.setCategorieColor("Work", workRGB);

        Color studyColor = studyCP.getValue();
        String studyRGB = getRGB(studyColor);
        databaseHandler.setCategorieColor("Study", studyRGB);

        Color sportColor = sportCP.getValue();
        String sportRGB = getRGB(sportColor);
        databaseHandler.setCategorieColor("Sport", sportRGB);

        Color vacationColor = vacationCP.getValue();
        String vacationRGB = getRGB(vacationColor);
        databaseHandler.setCategorieColor("Vacation", vacationRGB);

        Color birthdaysColor = birthdaysCP.getValue();
        String birthdaysRGB = getRGB(birthdaysColor);
        databaseHandler.setCategorieColor("Birthdays", birthdaysRGB);

        Color holidayColor = holidaysCP.getValue();
        String holidayRGB = getRGB(holidayColor);
        databaseHandler.setCategorieColor("Holiday", holidayRGB);

        Color otherColor = otherCP.getValue();
        String otherRGB = getRGB(otherColor);
        databaseHandler.setCategorieColor("Other", otherRGB);

    }

    private void initalizeColorPicker() {

        String workRGB = databaseHandler.getCategorieColor(databaseHandler.getCategorieID("Work"));
        String studyRGB = databaseHandler.getCategorieColor(databaseHandler.getCategorieID("Study"));
        String sportRGB = databaseHandler.getCategorieColor(databaseHandler.getCategorieID("Sport"));
        String vacationRGB = databaseHandler.getCategorieColor(databaseHandler.getCategorieID("Vacation"));
        String birthdayRGB = databaseHandler.getCategorieColor(databaseHandler.getCategorieID("Birthdays"));
        String holidayRGB = databaseHandler.getCategorieColor(databaseHandler.getCategorieID("Holidays"));
        String otherRGB = databaseHandler.getCategorieColor(databaseHandler.getCategorieID("Other"));

        // Parse for rgb values for work
        String[] colors = workRGB.split("-");
        String red = colors[0];
        String green = colors[1];
        String blue = colors[2];
        Color c = Color.rgb(Integer.parseInt(red), Integer.parseInt(green), Integer.parseInt(blue));
        workCP.setValue(c);

        // Parse for rgb values for study
        colors = sportRGB.split("-");
        red = colors[0];
        green = colors[1];
        blue = colors[2];
        c = Color.rgb(Integer.parseInt(red), Integer.parseInt(green), Integer.parseInt(blue));
        studyCP.setValue(c);

        // Parse for rgb values for sport
        colors = studyRGB.split("-");
        red = colors[0];
        green = colors[1];
        blue = colors[2];
        c = Color.rgb(Integer.parseInt(red), Integer.parseInt(green), Integer.parseInt(blue));
        sportCP.setValue(c);

        // Parse for rgb values for vacation
        colors = vacationRGB.split("-");
        red = colors[0];
        green = colors[1];
        blue = colors[2];
        c = Color.rgb(Integer.parseInt(red), Integer.parseInt(green), Integer.parseInt(blue));
        vacationCP.setValue(c);

        // Parse for rgb values for birthdays
        colors = birthdayRGB.split("-");
        red = colors[0];
        green = colors[1];
        blue = colors[2];
        c = Color.rgb(Integer.parseInt(red), Integer.parseInt(green), Integer.parseInt(blue));
        birthdaysCP.setValue(c);

        // Parse for rgb values for Holiday
        colors = holidayRGB.split("-");
        red = colors[0];
        green = colors[1];
        blue = colors[2];
        c = Color.rgb(Integer.parseInt(red), Integer.parseInt(green), Integer.parseInt(blue));
        holidaysCP.setValue(c);

        // Parse for rgb values for other
        colors = otherRGB.split("-");
        red = colors[0];
        green = colors[1];
        blue = colors[2];
        c = Color.rgb(Integer.parseInt(red), Integer.parseInt(green), Integer.parseInt(blue));
        otherCP.setValue(c);

    }

    public void initializeMonthView() {

        // Go through each calendar grid location, or each "day" (7x6)
        int rows = 6;
        int cols = 7;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                // Add VBox and style it
                // Add VBox and style it
                VBox vPane = new VBox();
                vPane.getStyleClass().add("calendar_pane");
                vPane.setMinWidth(weekdayHeader.getPrefWidth() / 7);

                vPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                    addEvent(vPane);
                });

                GridPane.setVgrow(vPane, Priority.ALWAYS);

                // Add it to the grid
                monthView.add(vPane, j, i);
            }
        }

        // Set up Row Constraints
        for (int i = 0; i < 7; i++) {
            RowConstraints row = new RowConstraints();
            row.setMinHeight(scrollPaneMonth.getHeight() / 7);
            monthView.getRowConstraints().add(row);
        }
    }


    public void initializeCalendarWeekdayHeader() {

        // 7 days in a week
        int weekdays = 7;

        //Weekday names
        String[] weekAbbr = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

        for (int i = 0; i < weekdays; i++) {

            // Make new pane and label of weekday
            StackPane pane = new StackPane();
            pane.getStyleClass().add("weekday-header");

            // Make panes take up equal space
            HBox.setHgrow(pane, Priority.ALWAYS);
            pane.setMaxWidth(Double.MAX_VALUE);

            // Note: After adding a label to this, it tries to resize itself..
            // So I'm setting a minimum width.
            pane.setMinWidth(weekdayHeader.getPrefWidth() / 7);

            // Add it to the header
            weekdayHeader.getChildren().add(pane);

            // Add weekday name
            pane.getChildren().add(new Label(weekAbbr[i]));
        }
    }

    //WEEK
    public void initializeCalendarWeekdayHeaderWeek() {

        //Weekday names
        String[] weekAbbr = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat / Sun"};

        for (int i = 0; i < weekAbbr.length; i++) {

            // Make new pane and label of weekday
            StackPane pane = new StackPane();
            pane.getStyleClass().add("weekday-header");

            // Make panes take up equal space
            HBox.setHgrow(pane, Priority.ALWAYS);
            pane.setMaxWidth(Double.MAX_VALUE);

            if (i < 3) { // from mon to wed =3
                pane.setMinWidth((weekdayHeaderForWeek.getPrefWidth() - 2 * labelDivider.getPrefWidth()) / 3);
                weekdayHeaderForWeek.getChildren().add(pane);
                pane.getChildren().add(new Label(weekAbbr[i]));

            } else { // from thu to sat/sun =3
                pane.setMinWidth((weekdayHeaderForWeek1.getPrefWidth() - 2 * labelDivider.getPrefWidth()) / 3);
                weekdayHeaderForWeek1.getChildren().add(pane);
                pane.getChildren().add(new Label(weekAbbr[i]));
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Make empty calendar in every view
        initializeDateSelector();

        initDateClock();

        initializeDayView();
        initializeCalendarDayHeader();

        initializeWeekView();
        initializeCalendarWeekdayHeaderWeek();

        initializeMonthView();
        initializeCalendarWeekdayHeader();

        initializeCalendarWeekdayHeaderYear();

        initializeYearView();


        // Set Depths
        JFXDepthManager.setDepth(scrollPaneMonth, 1);

        //*** Instantiate DBHandler object *******************
        databaseHandler = new DBHandler();
        //****************************************************

        //Initialize all Color Pickers. Show saved colors for specific categories
        initalizeColorPicker();

    }

    private void initializeCalendarWeekdayHeaderYear() {
        // Make new pane and label of weekday
        StackPane pane = new StackPane();
        pane.getStyleClass().add("weekday-header");

        // Make panes take up equal space
        HBox.setHgrow(pane, Priority.ALWAYS);
        pane.setMaxWidth(Double.MAX_VALUE);

        weekdayHeaderForYear.getChildren().add(pane);
        pane.setMinWidth(weekdayHeaderForYear.getPrefWidth());

        pane.getChildren().add(headersYearLbl);
    }

    private void initializeCalendarDayHeader() {
        StackPane pane = new StackPane();
        pane.getStyleClass().add("weekday-header");

        // Make panes take up equal space
        HBox.setHgrow(pane, Priority.ALWAYS);
        pane.setMaxWidth(Double.MAX_VALUE);

        dayHeaderForDay.getChildren().add(pane);
        pane.setMinWidth(dayHeaderForDay.getMaxWidth());
        pane.getChildren().add(headersDayLbl);

    }

    private void initDateClock() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            thisTime.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        String dayOfWeek = LocalDateTime.now().getDayOfWeek().toString();
        String month = LocalDateTime.now().getMonth().toString();
        int day = LocalDateTime.now().getDayOfMonth();
        int year = LocalDateTime.now().getYear();

        thisDate.setText(dayOfWeek + ", " + day + " " + month + " " + year);
    }

    private void initializeYearView() {

        //YearGrid - 3?4
        int monthCount = 1;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {


                VBox vPane = new VBox();
                vPane.getStyleClass().add("calendar_pane");
                vPane.setMinWidth(weekdayHeaderForYear.getPrefWidth() / 3);

                ImageView imView = new ImageView(new Image("calendar/ui/icons/icon_" + i + j + ".png"));
                imView.setFitHeight(95);
                imView.setFitWidth(95);
                vPane.getChildren().add(imView);
                vPane.setAlignment(Pos.CENTER);

                int month = i * 3 + j + 1;
                vPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                    if (selectedDate.getValue() != null) {
                        selectedDate.setValue(LocalDate.of(MyCalendar.getInstance().viewing_year, month, 1));
                        SelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
                        selectionModel.select(monthPan);
                    }
                });

                GridPane.setVgrow(vPane, Priority.ALWAYS);
                yearView.add(vPane, j, i);

                Label lbl = new Label(MyCalendar.getInstance().getMonth(monthCount));
                lbl.setTextFill(Color.web("#4682b4"));
                vPane.getChildren().add(lbl);
                monthCount++;
            }
        }
    }

    //WEEK
    private void initializeWeekView() {
        weekdayHeaderForWeek1.setMinWidth(weekdayHeaderForWeek.getPrefWidth());
        labelDivider1.setMinWidth(labelDivider.getPrefWidth());
        labelDivider2.setMinWidth(labelDivider.getPrefWidth());
        labelDivider3.setMinWidth(labelDivider.getPrefWidth());
        labelDivider4.setMinWidth(labelDivider.getPrefWidth());
        GridPane[] weekDayGrid = new GridPane[]{weekView, weekView1, weekView2, weekView3, weekView4, weekView5, weekView6};

        // Go through each grid, or each "day"
        for (int i = 0; i < 7; i++) {

            VBox vPane = new VBox();
            vPane.getStyleClass().add("calendar_pane");
            // header for mon, tue, wed (=3), 2 label for divide, 2 - border(
            vPane.setMinWidth((weekdayHeaderForWeek.getPrefWidth() - 2 * labelDivider.getPrefWidth()) / 3);

            vPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                addEvent(vPane);
            });

            GridPane.setVgrow(vPane, Priority.ALWAYS);
            weekDayGrid[i].add(vPane, 0, 0);
        }
    }

    private void initializeDayView() {

        GridPane[] DayGrid = new GridPane[]{dayView1, dayView2, dayView3};

        // Go through each grid, or each "day"
        for (int i = 0; i < 3; i++) {

            VBox vPane = new VBox();
            vPane.getStyleClass().add("calendar_pane");

/*            Label hourLbl = new Label(" " + i * 8 + ":00 - " + (i + 1) * 8 + ":00  ");
            hourLbl.setPadding(new Insets(5));
            hourLbl.setStyle("-fx-text-fill:darkslategray");
            vPane.getChildren().add(hourLbl);*/

            vPane.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                addEvent(vPane);
            });

            GridPane.setVgrow(vPane, Priority.ALWAYS);
            DayGrid[i].add(vPane, 0, 0);
        }
    }

    //**********************************************************************************
    //**********************************************************************************
    //**********************************************************************************

    // Side - menu buttons
    @FXML
    private void newCalendar(MouseEvent event) {
        newCalendarEvent();
    }

    @FXML
    private void manageCalendars(MouseEvent event) {
        listCalendarsEvent();
    }

    @FXML
    private void pdfBtn(MouseEvent event) {
        //exportCalendarPDF();
    }

    @FXML
    private void updateColors(MouseEvent event) {
        changeColors();

        if (MyCalendar.getInstance().calendar_name != null)
            repaintView();
    }

    //******************************************************************************************
    //******************************************************************************************
    //******************************************************************************************

    public void showFilteredEventsInMonth(ArrayList<String> filteredEventsList) {

        System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
        System.out.println("I am in the show filtered events in month function");
        System.out.println("The list of filted events is: " + filteredEventsList);
        System.out.println("****------******-------******--------");


        String calendarName = MyCalendar.getInstance().calendar_name;

        //String currentMonth = monthLbl.getText();
        //System.out.println("currentMonth is: " + currentMonth);
        //int currentMonthIndex = MyCalendar.getInstance().getMonthIndex(currentMonth) ;
        //System.out.println("currentMonthIndex is: " + currentMonthIndex);

        //int currentYear = Integer.parseInt(yearLbl.getText());
        //System.out.println("CurrentYear is: " + currentYear);

        System.out.println("Now, the events/labels will be shown/put on the calendar");
        System.out.println("****------******-------******--------");

        for (int i = 0; i < filteredEventsList.size(); i++) {
            String[] eventInfo = filteredEventsList.get(i).split("~");
            String eventDescript = eventInfo[1];
            int eventCategorieID = Integer.parseInt(eventInfo[2]);
            String eventCalName = eventInfo[3];
            String eventTime = eventInfo[5].substring(0, 5) + " - " + eventInfo[7].substring(0, 5);
            String eventDate = eventInfo[4];
            System.out.println(eventDescript);
            System.out.println(eventCategorieID);
            System.out.println(eventCalName);
            System.out.println(eventDate);

            String[] dateParts = eventDate.split("-");
            int eventYear = Integer.parseInt(dateParts[0]);
            int eventMonth = Integer.parseInt(dateParts[1]);
            int eventDay = Integer.parseInt(dateParts[2]);


            System.out.println("****------******-------******--------");
            System.out.println("Now I will check if currentYear equals eventYear. Result is:");
            if (true /*currentYear == eventYear*/) {
                System.out.println("Yes, both year match.");
                System.out.println("Now I will check if the month index equals the event's month. REsult is");
                // if (currentMonthIndex == eventMonth)
                {
                    System.out.println("Yes they are the same. Now I will call showDate function");
                    showDate(monthView, eventDay, eventTime, eventDescript, eventCategorieID);

                }
            }
        }
    }


    @FXML
    private void deleteAllEvents(MouseEvent event) {

        if (MyCalendar.getInstance() != null) {
            //Show confirmation dialog to make sure the user want to delete the selected rule
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("All Events Deletion");
            alert.setContentText("Are you sure you want to delete all events in this calendar?");
            //Customize the buttons in the confirmation dialog
            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");
            //Set buttons onto the confirmation dialog
            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

            //Get the user's answer on whether deleting or not
            Optional<ButtonType> result = alert.showAndWait();

            //If the user wants to delete the calendar, call the function that deletes the calendar. Otherwise, close the window
            if (result.get() == buttonTypeYes) {
                deleteAllEventsInCalendar();
            }
        } else {
            Alert alertMessage = new Alert(Alert.AlertType.INFORMATION);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Please create your calendar!");
            alertMessage.showAndWait();
        }
    }

    // Function that deletes all the events in the current calendar
    public void deleteAllEventsInCalendar() {

        //Variable that holds the name of the current calendar
        String calName = MyCalendar.getInstance().calendar_name;

        //Query that will delete all events that belong to the selected calendar
        String deleteAllEventsQuery = "DELETE FROM EVENTS "
                + "WHERE EVENTS.CalendarName='" + calName + "'";

        //Execute query that deletes all events associated to the selected calendar
        boolean eventsWereDeleted = databaseHandler.executeAction(deleteAllEventsQuery);

        //Check if events were successfully deleted and indicate the user if so
        if (eventsWereDeleted) {
            //Update the calendar. Show that events were actually deleted
            repaintView();

            //Show message indicating that the selected calendar was deleted
            Alert alertMessage = new Alert(Alert.AlertType.INFORMATION);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("All events were successfully deleted");
            alertMessage.showAndWait();
        } else {
            //Show message indicating that the calendar could not be deleted
            Alert alertMessage = new Alert(Alert.AlertType.ERROR);
            alertMessage.setHeaderText(null);
            alertMessage.setContentText("Deleting Events Failed!");
            alertMessage.showAndWait();
        }
    }


    public void initializeButtonPrevNext() {
        buttonPrevDay.setVisible(true);
        buttonNextDay.setVisible(true);
        buttonPrevWeek.setVisible(true);
        buttonNextWeek.setVisible(true);
        buttonPrevMonth.setVisible(true);
        buttonNextMonth.setVisible(true);
        buttonPrevYear.setVisible(true);
        buttonNextYear.setVisible(true);

        buttonPrevWeek.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

            LocalDate newValue = selectedDate.getValue().minusWeeks(1);
            selectedDate.setValue(newValue);
        });

        buttonNextWeek.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

            LocalDate newValue = selectedDate.getValue().plusWeeks(1);
            selectedDate.setValue(newValue);
        });

        buttonPrevMonth.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

            LocalDate newValue = selectedDate.getValue().minusMonths(1);
            selectedDate.setValue(newValue);
        });

        buttonNextMonth.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

            LocalDate newValue = selectedDate.getValue().plusMonths(1);
            selectedDate.setValue(newValue);
        });

        buttonPrevYear.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

            LocalDate newValue = selectedDate.getValue().minusYears(1);
            selectedDate.setValue(newValue);
        });

        buttonNextYear.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

            LocalDate newValue = selectedDate.getValue().plusYears(1);
            selectedDate.setValue(newValue);
        });

        buttonPrevDay.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

            LocalDate newValue = selectedDate.getValue().minusDays(1);
            selectedDate.setValue(newValue);
        });

        buttonNextDay.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {

            LocalDate newValue = selectedDate.getValue().plusDays(1);
            selectedDate.setValue(newValue);
        });
    }
}
