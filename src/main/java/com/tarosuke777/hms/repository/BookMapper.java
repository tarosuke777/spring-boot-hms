package com.tarosuke777.hms.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tarosuke777.hms.entity.BookEntity;

@Mapper
public interface BookMapper {

  public List<BookEntity> findMany();

  public BookEntity findOne(Integer bookId);

  public void insertOne(@Param("bookName") String bookName, @Param("authorId") Integer authorId, @Param("link") String link, @Param("note") String note);

  public void updateOne(
      @Param("bookId") Integer bookId,
      @Param("bookName") String bookName,
      @Param("authorId") Integer authorId,
      @Param("link") String link,
      @Param("note") String note);

  public int deleteOne(@Param("bookId") Integer bookId);
}
