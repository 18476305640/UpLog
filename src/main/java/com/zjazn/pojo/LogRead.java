package com.zjazn.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogRead {
    private Integer log_read_id;
    private Integer log_id;
    private Integer up_id;
    private Date create_date;

}
