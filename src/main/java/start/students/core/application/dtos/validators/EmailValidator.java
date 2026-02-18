package start.students.core.application.dtos.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Validador customizado para Email
 * Verifica se o email contém apenas caracteres ASCII válidos
 * Rejeita acentuação (á, é, í, ó, ú, ã, õ, etc.) e cedilha (ç)
 * Valida apenas domínios de email conhecidos e confiáveis
 */
public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    private static final Set<String> VALID_EMAIL_DOMAINS = new HashSet<>(Arrays.asList(
            "gmail.com",
            "outlook.com",
            "hotmail.com",
            "yahoo.com",
            "icloud.com",
            "mail.com",
            "protonmail.com",
            "tutanota.com",
            "zoho.com",
            "mailbox.org",
            "fastmail.com",
            "yandex.com",
            "aol.com",
            "mail.ru",
            "gmx.com",
            "gmx.net",
            "gmx.de",
            "web.de",
            "vodafone.it",
            "libero.it",
            "alice.it",
            "tim.it",
            "virgilio.it",
            "tiscali.it",
            "freenet.de",
            "t-online.de",
            "arcor.de",
            "wanadoo.de",
            "alice.de",
            "verizon.net",
            "comcast.net",
            "charter.net",
            "cox.net",
            "bellsouth.net",
            "earthlink.net",
            "sbcglobal.net",
            "att.net",
            "frontier.com",
            "windstream.net",
            "1and1.com"
    ));

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // Deixar outras validações (como @NotBlank) tratarem valores nulos
        }

        String email = value.trim().toLowerCase();

        // Regex para detectar acentos e cedilha
        String accentPattern = "[áàâãäéèêëíìîïóòôõöúùûüçÁÀÂÃÄÉÈÊËÍÌÎÏÓÒÔÕÖÚÙÛÜÇ]";
        if (email.matches(".*" + accentPattern + ".*")) {
            addConstraintViolation(context, "Email não pode conter caracteres acentuados ou cedilha");
            return false;
        }

        // Regex para validar email ASCII puro (sem acentos ou ç)
        String validEmailRegex = "^[a-zA-Z0-9._@+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$";
        if (!email.matches(validEmailRegex)) {
            addConstraintViolation(context, "Email deve ter um formato válido");
            return false;
        }

        // Extrair domínio do email
        String[] emailParts = email.split("@");
        if (emailParts.length != 2) {
            addConstraintViolation(context, "Email deve ter um formato válido");
            return false;
        }

        String domain = emailParts[1];

        // Verificar se o domínio está na lista de provedores válidos
        if (!VALID_EMAIL_DOMAINS.contains(domain)) {
            addConstraintViolation(context, "Provedor de email não suportado. Use: Gmail, Outlook, Yahoo, iCloud ou outro provedor conhecido");
            return false;
        }

        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }
}
