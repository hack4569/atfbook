package com.shsb.atfbook.domain.shared;

import org.springframework.ui.Model;

import java.util.Map;

public class SpringUtils {
    public static void addAttributes(Model model, Map<String, Object> attrs) {
        attrs.forEach(model::addAttribute);
    }
}
