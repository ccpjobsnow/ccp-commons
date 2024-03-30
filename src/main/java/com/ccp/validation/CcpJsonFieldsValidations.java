package com.ccp.validation;

import java.util.Arrays;
import java.util.Map;

import com.ccp.constantes.CcpConstants;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.validation.annotations.AllowedValues;
import com.ccp.validation.annotations.ArrayNumbers;
import com.ccp.validation.annotations.ArraySize;
import com.ccp.validation.annotations.ArrayTextSize;
import com.ccp.validation.annotations.Day;
import com.ccp.validation.annotations.ObjectNumbers;
import com.ccp.validation.annotations.SimpleObject;
import com.ccp.validation.annotations.ObjectTextSize;
import com.ccp.validation.annotations.Regex;
import com.ccp.validation.annotations.ValidationRules;
import com.ccp.validation.annotations.Year;
import com.ccp.validation.enums.AllowedValuesValidations;
import com.ccp.validation.enums.BoundValidations;
import com.ccp.validation.enums.SimpleObjectValidations;

public class CcpJsonFieldsValidations {
	
	public static void validate(Class<?> clazz, Map<String, Object> map) {
		
		boolean isNotPresent = clazz.isAnnotationPresent(ValidationRules.class) == false;
		
		if(isNotPresent) {
			return;
		}
		
		ValidationRules rules = clazz.getAnnotation(ValidationRules.class);
		validate(rules, map);
	}
	
	public static void validate(ValidationRules rules, Map<String, Object> map) {
		
		CcpJsonRepresentation json = new CcpJsonRepresentation(map);

		Class<?> rulesClass = rules.rulesClass();
		
		rules = rulesClass.isAnnotationPresent(ValidationRules.class) ? rulesClass.getAnnotation(ValidationRules.class) : rules;
		
		CcpJsonRepresentation errors = CcpConstants.EMPTY_JSON;

		errors = validateBounds(rules, json, errors);

		errors = validateRestricted(rules, json, errors);

		errors = simpleValidation(rules, json, errors);

		boolean noErrors = errors.isEmpty();

		if (noErrors) {
			return;
		}
		
		CcpJsonRepresentation result = CcpConstants.EMPTY_JSON.put("errors", errors).put("json", json);

		throw new CcpJsonInvalid(result);
	}

	private static CcpJsonRepresentation simpleValidation(ValidationRules rules, CcpJsonRepresentation json,
			CcpJsonRepresentation result) {
		SimpleObject[] simples = rules.simpleObject();

		for (SimpleObject validation : simples) {
			String[] fields = validation.fields();
			SimpleObjectValidations rule = validation.rule();
			boolean validJson = rule.isValidJson(json, fields);

			if (validJson) {
				continue;
			}
			result = addErrorDetail(result, "none", rule,  fields);
		}
		return result;
	}

	private static CcpJsonRepresentation validateRestricted(ValidationRules rules, CcpJsonRepresentation json,
			CcpJsonRepresentation result) {
		AllowedValues[] restricteds = rules.allowedValues();

		for (AllowedValues validation : restricteds) {
			String[] restrictedValues = validation.allowedValues();
			String[] fields = validation.fields();
			AllowedValuesValidations rule = validation.rule();
			boolean validJson = rule.isValidJson(json, restrictedValues, fields);

			if (validJson) {
				continue;
			}
			result = addErrorDetail(result, restrictedValues, rule,  fields);
		}
		return result;
	}

	
	private static CcpJsonRepresentation addErrorDetail(CcpJsonRepresentation result, Object bound, BoundValidations rule, String... fields) {
		String ruleName = rule.getClass().getSimpleName().split("$")[0];
		result = result.putSubKey(ruleName, "description", rule.name());
		result = result.putSubKey(ruleName, "specification", bound);
		result = result.putSubKey(ruleName, "fields", Arrays.asList(fields));
		return result;
	}
	
	private static CcpJsonRepresentation addErrorDetail(CcpJsonRepresentation result, Object bound, Enum<?> rule, String... fields) {
		String ruleName = rule.getClass().getSimpleName().split("$")[0];
		result = result.putSubKey(ruleName, "description", rule.name());
		result = result.putSubKey(ruleName, "specification", bound);
		result = result.putSubKey(ruleName, "fields", Arrays.asList(fields));
		return result;
	}
	
	private static CcpJsonRepresentation validateBounds(ValidationRules rules, CcpJsonRepresentation json,
			CcpJsonRepresentation result) {

		{
			ArrayNumbers[] x1 = rules.arrayNumbers();

			for (ArrayNumbers validation : x1) {
				double bound = validation.bound();
				String[] fields = validation.fields();
				BoundValidations rule = validation.rule();
				boolean validJson = rule.isValidJson(json, bound, fields);

				if (validJson) {
					continue;
				}
				result = addErrorDetail(result, bound, rule,  fields);
			}
			
		}

		{
			ArraySize[] x1 = rules.arraySize();

			for (ArraySize validation : x1) {
				double bound = validation.bound();
				String[] fields = validation.fields();
				BoundValidations rule = validation.rule();
				boolean validJson = rule.isValidJson(json, bound, fields);

				if (validJson) {
					continue;
				}
				result = addErrorDetail(result, bound, rule,  fields);
			}

		}
		{
			ArrayTextSize[] x1 = rules.arrayTextSize();

			for (ArrayTextSize validation : x1) {
				double bound = validation.bound();
				String[] fields = validation.fields();
				BoundValidations rule = validation.rule();
				boolean validJson = rule.isValidJson(json, bound, fields);

				if (validJson) {
					continue;
				}
				result = addErrorDetail(result, bound, rule,  fields);
			}

		}
		{
			ObjectNumbers[] x1 = rules.objectNumbers();

			for (ObjectNumbers validation : x1) {
				double bound = validation.bound();
				String[] fields = validation.fields();
				BoundValidations rule = validation.rule();
				boolean validJson = rule.isValidJson(json, bound, fields);

				if (validJson) {
					continue;
				}
				result = addErrorDetail(result, bound, rule,  fields);
			}

		}
		{
			ObjectTextSize[] x1 = rules.objectTextSize();

			for (ObjectTextSize validation : x1) {
				double bound = validation.bound();
				String[] fields = validation.fields();
				BoundValidations rule = validation.rule();
				boolean validJson = rule.isValidJson(json, bound, fields);

				if (validJson) {
					continue;
				}
				result = addErrorDetail(result, bound, rule,  fields);
			}

		}
		{
			Day[] x1 = rules.day();

			for (Day validation : x1) {
				double bound = validation.bound();
				String[] fields = validation.fields();
				BoundValidations rule = validation.rule();
				boolean validJson = rule.isValidJson(json, bound, fields);

				if (validJson) {
					continue;
				}
				result = addErrorDetail(result, bound, rule,  fields);
			}

		}
		{
			Year[] x1 = rules.year();

			for (Year validation : x1) {
				double bound = validation.bound();
				String[] fields = validation.fields();
				BoundValidations rule = validation.rule();
				boolean validJson = rule.isValidJson(json, bound, fields);

				if (validJson) {
					continue;
				}
				result = addErrorDetail(result, bound, rule,  fields);
			}

		}
		
		{
			Regex[] x1 = rules.regex();
			
			for (Regex validation : x1) {
				String[] fields = validation.fields();
				String regex = validation.value();
				boolean validJson = json.itIsTrueThatTheFollowingFields(fields)
				.ifTheyAreAll().textsThenEachOneMatchesWithTheFollowingRegex(regex);
				
				if (validJson) {
					continue;
				}
				
				result = result.putSubKey("regexValidation", "fields", Arrays.asList(fields));
				result = result.putSubKey("regexValidation", "specification", regex);
			}
		}
		
		return result;
	}
	@SuppressWarnings("serial")
	public static class CcpJsonInvalid extends RuntimeException {

		public final CcpJsonRepresentation result;

		private CcpJsonInvalid(CcpJsonRepresentation result) {
			this.result = result;
		}
	}


}
