package lk.bit.web.entity;

import java.time.LocalDateTime;

public interface CustomEntity6 {

    String getComplainId();

    String getComplainSubject();

    String getComplainDesc();

    LocalDateTime getComplainCreatedDate();

    LocalDateTime getSolutionCreatedDate();

    String getSolutionDesc();

    int getSolutionStatus();
}
