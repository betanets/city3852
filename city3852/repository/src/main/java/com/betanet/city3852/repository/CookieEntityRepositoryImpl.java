/*
 * The MIT License
 *
 * Copyright 2017 Alexander Shkirkov.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.betanet.city3852.repository;

import com.betanet.city3852.domain.cookieentity.CookieEntity;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Alexander Shkirkov
 */
@Repository
public class CookieEntityRepositoryImpl implements CookieEntityRepository{
    @PersistenceContext
    private EntityManager entityManager;
    
    @Transactional
    @Override
    public void save(CookieEntity cookieEntity) {
        //Merge entity to avoid duplication and detached entity error
        entityManager.merge(cookieEntity);
    }

    @Override
    public CookieEntity getCookieByKey(String cookieKey) {
        List<CookieEntity> results = entityManager.createQuery(
            "SELECT ce FROM CookieEntity ce WHERE ce.cookieKey = :cookieKey", CookieEntity.class)
            .setParameter("cookieKey", cookieKey)
            .getResultList();
        return (results == null || results.isEmpty()) ? null : results.get(0);
    }

    @Override
    public List<CookieEntity> getAllCookies() {
        return entityManager.createQuery("SELECT ce FROM CookieEntity ce", CookieEntity.class)
            .getResultList();
    }
}
