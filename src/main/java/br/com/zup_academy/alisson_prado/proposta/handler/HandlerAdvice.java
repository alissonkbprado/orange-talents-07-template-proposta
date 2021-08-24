package br.com.zup_academy.alisson_prado.proposta.handler;

import br.com.zup_academy.alisson_prado.proposta.exception.ApiErroException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestControllerAdvice
public class HandlerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroPadronizado> handle(MethodArgumentNotValidException exception) {
        Collection<String> mensagens = new ArrayList<>();
        BindingResult bindingResult = exception.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        fieldErrors.forEach(fieldError -> {
            String message = String.format("Campo %s %s", fieldError.getField(), fieldError.getDefaultMessage());
            mensagens.add(message);
        });

        ErroPadronizado erroPadronizado = new ErroPadronizado(mensagens);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroPadronizado);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErroPadronizado> handle(BindException exception) {
        Collection<String> mensagens = new ArrayList<>();
        BindingResult bindingResult = exception.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        fieldErrors.forEach(fieldError -> {
            String message = String.format("Campo %s %s", fieldError.getField(), fieldError.getDefaultMessage());
            mensagens.add(message);
        });
        ErroPadronizado erroPadronizado = new ErroPadronizado(mensagens);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroPadronizado);
    }

    @ExceptionHandler(ApiErroException.class)
    public ResponseEntity<ErroPadronizado> handleApiErroException(ApiErroException apiErroException) {
        Collection<String> mensagens = new ArrayList<>();
        mensagens.add(apiErroException.getReason());

        ErroPadronizado erroPadronizado = new ErroPadronizado(mensagens);
        return ResponseEntity.status(apiErroException.getHttpStatus()).body(erroPadronizado);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErroPadronizado> handle(IllegalStateException exception){
        Collection<String> mensagens = new ArrayList<>();
        String message = String.format("Campo %s %s", exception.getLocalizedMessage(), "Formato de entrada de dados inválido");
        mensagens.add(message);
        ErroPadronizado erroPadronizado = new ErroPadronizado(mensagens);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroPadronizado);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroPadronizado> handle(HttpMessageNotReadableException exception){
        Collection<String> mensagens = new ArrayList<>();
        String message = String.format("Campo %s %s", exception.getLocalizedMessage(), "Corpo da requisição inválido");
        mensagens.add(message);
        ErroPadronizado erroPadronizado = new ErroPadronizado(mensagens);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroPadronizado);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroPadronizado> handle(IllegalArgumentException exception){
        Collection<String> mensagens = new ArrayList<>();
        String message = String.format("Campo %s %s", exception.getLocalizedMessage());
        mensagens.add(message);
        ErroPadronizado erroPadronizado = new ErroPadronizado(mensagens);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroPadronizado);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErroPadronizado> handle(ConstraintViolationException exception){
        Collection<String> mensagens = new ArrayList<>();
        String message = String.format("Campo %s %s", exception.getLocalizedMessage());
        mensagens.add(message);
        ErroPadronizado erroPadronizado = new ErroPadronizado(mensagens);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroPadronizado);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErroPadronizado> handle(HttpRequestMethodNotSupportedException exception){
        Collection<String> mensagens = new ArrayList<>();
        String message = String.format(exception.getLocalizedMessage());
        mensagens.add(message);
        ErroPadronizado erroPadronizado = new ErroPadronizado(mensagens);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(erroPadronizado);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErroPadronizado> handle(MissingServletRequestParameterException exception){
        Collection<String> mensagens = new ArrayList<>();
        String message = String.format("Campo %s ", exception.getLocalizedMessage());
        mensagens.add(message);
        ErroPadronizado erroPadronizado = new ErroPadronizado(mensagens);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroPadronizado);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ErroPadronizado> handle(MissingPathVariableException exception){
        Collection<String> mensagens = new ArrayList<>();
        String message = String.format("Campo %s ", exception.getParameter().getParameter().getName() + " não foi enviado pela URL");
        mensagens.add(message);
        ErroPadronizado erroPadronizado = new ErroPadronizado(mensagens);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroPadronizado);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErroPadronizado> handle(NullPointerException exception){
        Collection<String> mensagens = new ArrayList<>();
        String message = String.format(exception.getMessage());
        mensagens.add(message);
        ErroPadronizado erroPadronizado = new ErroPadronizado(mensagens);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroPadronizado);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErroPadronizado> handleResponseStatusException(ResponseStatusException responseStatusException) {
        Collection<String> mensagens = new ArrayList<>();
        mensagens.add(responseStatusException.getReason());

        ErroPadronizado erroPadronizado = new ErroPadronizado(mensagens);
        return ResponseEntity.status(responseStatusException.getStatus()).body(erroPadronizado);
    }
}
