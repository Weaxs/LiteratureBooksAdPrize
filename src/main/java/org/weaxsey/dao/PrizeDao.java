package org.weaxsey.dao;

import org.weaxsey.domain.Prize;

import java.util.List;

/**
 * prize dao
 *
 * @author Weaxs
 */
public interface PrizeDao {

    /**
     * insert prize message
     * @param prizes prizes
     */
    void insertPrizes(List<Prize> prizes);


}
