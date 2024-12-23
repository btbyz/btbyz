package com.ruoyi.flowable.domain.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CalendarificResponse {
    private String name;

    private String description;

    private Country country;

    private Date date;

    private List<String> type;

    @Data
    public static class Country {
        private String id;
        private String name;
    }
    @Data
    public static class Date {
        private String iso;
        private DateTime datetime;

        @Data
        public static class DateTime {
            private int year;
            private int month;
            private int day;
        }
    }

}


