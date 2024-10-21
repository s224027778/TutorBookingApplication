package com.example.cc;

public class Booking {
    private int id;
    private String tutorName;
    private String studentName;
    private String moduleName;
    private String date;
    private String time;
    private String duration;
    private int status;

    public Booking(int id, int status, String duration, String time, String date, String moduleName, String studentName, String tutorName) {
        this.id = id;
        this.status = status;
        this.duration = duration;
        this.time = time;
        this.date = date;
        this.moduleName = moduleName;
        this.studentName = studentName;
        this.tutorName = tutorName;
    }

    public int getId() { return id; }
    public int getStatus() { return status; }
    public String getTutorName() { return tutorName; }
    public String getStudentName() { return studentName; }
    public String getModuleName() { return moduleName; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getDuration() { return duration; }

}
