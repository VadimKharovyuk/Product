package com.example.product.util;



import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

@Component
public class SlugUtil {
    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern MULTIPLE_DASHES = Pattern.compile("-+");

    public static String generateSlug(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "category-" + UUID.randomUUID().toString().substring(0, 8);
        }

        // Сначала транслитерируем кириллицу в латиницу
        String transliterated = transliterate(input);

        String nowhitespace = WHITESPACE.matcher(transliterated).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        slug = MULTIPLE_DASHES.matcher(slug).replaceAll("-");
        slug = slug.toLowerCase(Locale.ENGLISH);
        slug = slug.trim();

        // Проверка на пустой слаг после обработки
        if (slug.isEmpty()) {
            slug = "category-" + UUID.randomUUID().toString().substring(0, 8);
        }

        if (slug.length() > 100) {
            slug = slug.substring(0, 100);
        }

        return slug;
    }

    private static String transliterate(String input) {
        Map<Character, String> charMap = new HashMap<>();
        // Русские строчные буквы
        charMap.put('а', "a"); charMap.put('б', "b"); charMap.put('в', "v");
        charMap.put('г', "g"); charMap.put('д', "d"); charMap.put('е', "e");
        charMap.put('ё', "yo"); charMap.put('ж', "zh"); charMap.put('з', "z");
        charMap.put('и', "i"); charMap.put('й', "y"); charMap.put('к', "k");
        charMap.put('л', "l"); charMap.put('м', "m"); charMap.put('н', "n");
        charMap.put('о', "o"); charMap.put('п', "p"); charMap.put('р', "r");
        charMap.put('с', "s"); charMap.put('т', "t"); charMap.put('у', "u");
        charMap.put('ф', "f"); charMap.put('х', "h"); charMap.put('ц', "ts");
        charMap.put('ч', "ch"); charMap.put('ш', "sh"); charMap.put('щ', "sch");
        charMap.put('ъ', ""); charMap.put('ы', "y"); charMap.put('ь', "");
        charMap.put('э', "e"); charMap.put('ю', "yu"); charMap.put('я', "ya");

        // Русские заглавные буквы
        charMap.put('А', "A"); charMap.put('Б', "B"); charMap.put('В', "V");
        charMap.put('Г', "G"); charMap.put('Д', "D"); charMap.put('Е', "E");
        charMap.put('Ё', "Yo"); charMap.put('Ж', "Zh"); charMap.put('З', "Z");
        charMap.put('И', "I"); charMap.put('Й', "Y"); charMap.put('К', "K");
        charMap.put('Л', "L"); charMap.put('М', "M"); charMap.put('Н', "N");
        charMap.put('О', "O"); charMap.put('П', "P"); charMap.put('Р', "R");
        charMap.put('С', "S"); charMap.put('Т', "T"); charMap.put('У', "U");
        charMap.put('Ф', "F"); charMap.put('Х', "H"); charMap.put('Ц', "Ts");
        charMap.put('Ч', "Ch"); charMap.put('Ш', "Sh"); charMap.put('Щ', "Sch");
        charMap.put('Ъ', ""); charMap.put('Ы', "Y"); charMap.put('Ь', "");
        charMap.put('Э', "E"); charMap.put('Ю', "Yu"); charMap.put('Я', "Ya");

        StringBuilder sb = new StringBuilder(input.length());
        for (char c : input.toCharArray()) {
            String replacement = charMap.get(c);
            if (replacement != null) {
                sb.append(replacement);
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }
}