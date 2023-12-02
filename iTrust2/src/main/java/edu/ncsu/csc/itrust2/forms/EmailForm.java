package edu.ncsu.csc.itrust2.forms;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailForm {
    private String sender;
    private String receiver;
    private String subject;
    private String messageBody;
}
