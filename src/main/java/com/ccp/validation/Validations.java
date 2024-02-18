package com.ccp.validation;

import java.util.Map;

import com.ccp.constantes.CcpConstants;
import com.ccp.decorators.CcpJsonRepresentation;
import com.ccp.validation.annotations.AllowedValues;
import com.ccp.validation.annotations.ArrayNumbers;
import com.ccp.validation.annotations.ArraySize;
import com.ccp.validation.annotations.ArrayTexts;
import com.ccp.validation.annotations.ObjectNumbers;
import com.ccp.validation.annotations.ObjectRules;
import com.ccp.validation.annotations.ObjectText;
import com.ccp.validation.annotations.ValidationRules;
import com.ccp.validation.enums.AllowedValuesValidations;
import com.ccp.validation.enums.BoundValidations;
import com.ccp.validation.enums.ObjectValidations;

public class Validations {
	public static void validate(ValidationRules rules, Map<String, Object> map) {
		CcpJsonRepresentation json = new CcpJsonRepresentation(map);

		Class<?> rulesClass = rules.rulesClass();
		
		rules = rulesClass.isAnnotationPresent(ValidationRules.class) ? rulesClass.getAnnotation(ValidationRules.class) : rules;
		
		CcpJsonRepresentation result = CcpConstants.EMPTY_JSON;

		result = validateBounds(rules, json, result);

		result = validateRestricted(rules, json, result);

		result = validateSimples(rules, json, result);

		boolean noErrors = result.isEmpty();

		if (noErrors) {
			return;
		}

		throw new CcpJsonInvalid(result);
	}

	private static CcpJsonRepresentation validateSimples(ValidationRules rules, CcpJsonRepresentation json,
			CcpJsonRepresentation result) {
		ObjectRules[] simples = rules.simpleObjectRules();

		for (ObjectRules validation : simples) {
			String[] fields = validation.fields();
			ObjectValidations rule = validation.rule();
			boolean validJson = rule.isValidJson(json, fields);

			if (validJson) {
				continue;
			}
			result = result.addToList(rule.name(), (Object[]) fields);
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
			result = result.addToList(rule.name(), (Object[]) fields);
		}
		return result;
	}

	private static CcpJsonRepresentation validateBounds(ValidationRules rules, CcpJsonRepresentation json,
			CcpJsonRepresentation result) {

		{
			ArrayNumbers[] x1 = rules.arrayNumbersValidations();

			for (ArrayNumbers validation : x1) {
				double bound = validation.bound();
				String[] fields = validation.fields();
				BoundValidations rule = validation.rule();
				boolean validJson = rule.isValidJson(json, bound, fields);

				if (validJson) {
					continue;
				}
				result = result.addToList(rule.name(), (Object[]) fields);
			}

		}

		{
			ArraySize[] x1 = rules.arraySizeValidations();

			for (ArraySize validation : x1) {
				double bound = validation.bound();
				String[] fields = validation.fields();
				BoundValidations rule = validation.rule();
				boolean validJson = rule.isValidJson(json, bound, fields);

				if (validJson) {
					continue;
				}
				result = result.addToList(rule.name(), (Object[]) fields);
			}

		}
		{
			ArrayTexts[] x1 = rules.arrayTextsValidations();

			for (ArrayTexts validation : x1) {
				double bound = validation.bound();
				String[] fields = validation.fields();
				BoundValidations rule = validation.rule();
				boolean validJson = rule.isValidJson(json, bound, fields);

				if (validJson) {
					continue;
				}
				result = result.addToList(rule.name(), (Object[]) fields);
			}

		}
		{
			ObjectNumbers[] x1 = rules.objectNumbersValidations();

			for (ObjectNumbers validation : x1) {
				double bound = validation.bound();
				String[] fields = validation.fields();
				BoundValidations rule = validation.rule();
				boolean validJson = rule.isValidJson(json, bound, fields);

				if (validJson) {
					continue;
				}
				result = result.addToList(rule.name(), (Object[]) fields);
			}

		}
		{
			ObjectText[] x1 = rules.objectTextsValidations();

			for (ObjectText validation : x1) {
				double bound = validation.bound();
				String[] fields = validation.fields();
				BoundValidations rule = validation.rule();
				boolean validJson = rule.isValidJson(json, bound, fields);

				if (validJson) {
					continue;
				}
				result = result.addToList(rule.name(), (Object[]) fields);
			}

		}
		return result;
	}
	@SuppressWarnings("serial")
	public static class CcpJsonInvalid extends RuntimeException {

		public final CcpJsonRepresentation errors;

		private CcpJsonInvalid(CcpJsonRepresentation errors) {
			this.errors = errors;
		}
	}


}
