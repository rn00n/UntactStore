package com.untacstore.modules.account;

import com.querydsl.core.types.Predicate;
import com.untacstore.modules.keyword.Keyword;

import java.util.List;

public class AccountPredicates {
    public static Predicate findByKeywords(List<Keyword> keywords) {
        return QAccount.account.keywords.any().in(keywords);
    }
}
