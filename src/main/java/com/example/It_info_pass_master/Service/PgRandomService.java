package com.example.It_info_pass_master.Service;

import com.example.It_info_pass_master.DAO.RandomDao;
import com.example.It_info_pass_master.Entity.SelectRandomRecord;
import com.example.It_info_pass_master.Entity.CategoryRecord;
import com.example.It_info_pass_master.Entity.QuestionRecord;
import com.example.It_info_pass_master.Entity.UserCategoryRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PgRandomService implements RandomService{
    @Autowired
    private RandomDao randomDao;

    @Override
    public List<CategoryRecord> findRandSelect(int ageId, int userId){
        return randomDao.findRandSelect(ageId, userId);
    }

    @Override
    public List<QuestionRecord> selectRandom(SelectRandomRecord selectRandomRecord){
        return randomDao.selectRandom(selectRandomRecord);
    }

    @Override
    public int categorySelectUpdate(UserCategoryRecord userCategoryRecord) {
        return randomDao.categorySelectUpdate(userCategoryRecord);
    }
}
