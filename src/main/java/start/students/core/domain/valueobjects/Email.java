package start.students.core.domain.valueobjects;

import lombok.Value;
import start.students.core.domain.exceptions.DomainException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Value
public class Email {
    String value;

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

    public Email(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new DomainException("Email não pode ser vazio");
        }

        if (!isValidEmail(value.trim())) {
            throw new DomainException("Email inválido ou provedor não suportado");
        }

        this.value = value.trim().toLowerCase();
    }

    private boolean isValidEmail(String email) {
        String emailLower = email.toLowerCase();

        // Rejeita acentos e cedilha
        if (emailLower.matches(".*[áàâãäéèêëíìîïóòôõöúùûüçÁÀÂÃÄÉÈÊËÍÌÎÏÓÒÔÕÖÚÙÛÜÇ].*")) {
            return false;
        }

        // Valida formato ASCII puro
        if (!emailLower.matches("^[a-zA-Z0-9._@+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$")) {
            return false;
        }

        // Extrai domínio
        String[] emailParts = emailLower.split("@");
        if (emailParts.length != 2) {
            return false;
        }

        String domain = emailParts[1];

        // Verifica se o domínio está na lista de provedores válidos
        return VALID_EMAIL_DOMAINS.contains(domain);
    }
}
