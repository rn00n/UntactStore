package com.untacstore.modules.account;

import com.querydsl.core.types.Predicate;
import com.untacstore.modules.keyword.Keyword;

import java.util.List;
import java.util.Set;

public class AccountPredicates {
    public static Predicate findByKeywords(Set<Keyword> keywords) {
        return QAccount.account.keywords.any().in(keywords);
    }
}
