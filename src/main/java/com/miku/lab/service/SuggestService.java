package com.miku.lab.service;/*
 *@author miku
 *@data 2021/7/9 14:57
 *@version:1.1
 */

import com.miku.lab.entity.Suggestion;
import org.springframework.stereotype.Service;

@Service
public interface SuggestService {

    Object getAllSuggestion();

    int addSuggest(Suggestion suggestion);
}
