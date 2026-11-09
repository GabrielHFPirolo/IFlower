package com.iflower.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
<<<<<<< HEAD

=======
>>>>>>> 30e1cb06bcc198f0b560a7b5897187a186905d87
import java.time.Instant;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
<<<<<<< HEAD
public record ApiError(Instant timestamp, int status, String mensagem, Map<String, String> campos) {
}
=======
public record ApiError(
    Instant timestamp,
    int status,
    String mensagem,
    Map<String, String> campos
) {}
>>>>>>> 30e1cb06bcc198f0b560a7b5897187a186905d87
