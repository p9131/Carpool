package com.example.mile.history;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HistoryItem {
        private String Status;
        private String date;
        private int Order_id;

    Calendar calendar = Calendar.getInstance();
    Date today = calendar.getTime();

    // Format the date to a string
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    String todayString = dateFormat.format(today);

        public HistoryItem(String HistoryName,int id) {
            this.Status = HistoryName;
            this.date = todayString;
            this.Order_id=id;
        }

        public String getStatus() {
            return Status;
        }

        public String getDate() {
            return date;
        }

    public String getOrder_id() {
        return String.valueOf(Order_id);
    }

}
