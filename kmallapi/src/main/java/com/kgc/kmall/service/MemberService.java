package com.kgc.kmall.service;

import com.kgc.kmall.bean.Member;

import java.util.List;

/**
 * @author 张康硕
 * @create 2020-12-15 16:16
 */
public interface MemberService {
    List<Member> selectAllMember();
}
