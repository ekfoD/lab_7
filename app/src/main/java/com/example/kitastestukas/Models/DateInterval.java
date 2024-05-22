package com.example.kitastestukas.Models;

import java.time.LocalDate;

    public class DateInterval {
        private LocalDate dateBegin;
        private LocalDate dateEnd;

        public DateInterval(LocalDate dateBegin, LocalDate dateEnd) {
            setDateBegin(dateBegin);
            setDateEnd(dateEnd);
        }
        public LocalDate getDateBegin() {
            return dateBegin;
        }
        public void setDateBegin(LocalDate dateBegin) {
            this.dateBegin = dateBegin;
        }

        public LocalDate getDateEnd() {
            return dateEnd;
        }
        public void setDateEnd(LocalDate dateEnd) {
            this.dateEnd = dateEnd;
        }
    }

