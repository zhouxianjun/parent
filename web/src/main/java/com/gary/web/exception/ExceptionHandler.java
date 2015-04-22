package com.gary.web.exception;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;

import com.gary.web.exception.error.ErrorCode;
import com.gary.web.result.ExecuteResult;
import com.gary.web.result.ParameterError;
import com.gary.web.result.RequestParameterError;
import com.gary.web.result.Result;

public abstract class ExceptionHandler {
	protected Result exceptionHandler(Exception ex, Map<String, Integer> exceptionMap, Locale locale){
		String className = ex.getClass().getName();
		Result result = new Result();
		result.setMsg(ex.getMessage());
		result.setSuccess(false);
		if(exceptionMap != null && exceptionMap.containsKey(className)){
			Integer code = exceptionMap.get(className);
			result.setExecuteResult(new ExecuteResult(code, locale));
		}else{
			validationException(result, className, ex);
		}
		return result;
	}
	protected void validationException(Result result, String className, Exception ex){
		if("javax.validation.ConstraintViolationException".equalsIgnoreCase(className)){
			result.setExecuteResult(new ExecuteResult(ErrorCode.PARAM_FAIL));
			ConstraintViolationException cve = (ConstraintViolationException)ex;
			Set<ConstraintViolation<?>> set = cve.getConstraintViolations();
			Set<ParameterError> errors = new HashSet<ParameterError>();
			for (ConstraintViolation<?> constraintViolation : set) {
				ParameterError pe = new ParameterError();
				pe.setClassName(constraintViolation.getRootBeanClass().getName());
				pe.setErrorMsg(constraintViolation.getMessage());
				pe.setName(constraintViolation.getPropertyPath().toString());
				pe.setValue(constraintViolation.getInvalidValue());
				errors.add(pe);
			}
			result.getData().put("validation", errors);
		}else if("org.springframework.web.bind.MissingServletRequestParameterException".equalsIgnoreCase(className)){
			result.setExecuteResult(new ExecuteResult(ErrorCode.PARAM_FAIL));
			MissingServletRequestParameterException msrpe = (MissingServletRequestParameterException)ex;
			RequestParameterError rpe = new RequestParameterError();
			rpe.setName(msrpe.getParameterName());
			rpe.setType(msrpe.getParameterType());
			rpe.setErrorMsg(msrpe.getMessage());
			result.getData().put("requestParameter", rpe);
		}else if("org.springframework.validation.BindException".equalsIgnoreCase(className)){
			BindException bindException = (BindException)ex;
			List<FieldError> list = bindException.getFieldErrors();
			Set<ParameterError> errors = new HashSet<ParameterError>();
			result.setExecuteResult(new ExecuteResult(ErrorCode.PARAM_FAIL));
			for (FieldError fieldError : list) {
				ParameterError pe = new ParameterError();
				pe.setClassName(bindException.getFieldType(fieldError.getField()).getName());
				pe.setErrorMsg(fieldError.getDefaultMessage());
				pe.setName(fieldError.getField());
				pe.setValue(bindException.getFieldValue(fieldError.getField()));
				errors.add(pe);
			}
			result.getData().put("validation", errors);
		}else{
			result.setExecuteResult(new ExecuteResult(ErrorCode.UNKNOWN_ERROR));
		}
	}
}
