package acc.hotsix.file_share.dao;

import acc.hotsix.file_share.dto.FileQueryRequestDTO;
import acc.hotsix.file_share.dto.FileQuerySearchResponseDTO;
import acc.hotsix.file_share.dto.FileSearchRequestDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDate;
import java.util.List;

import static acc.hotsix.file_share.domain.QFile.file;

@RequiredArgsConstructor
public class FileCustomRepositoryImpl implements FileCustomRepository{
    private final JPAQueryFactory queryFactory;

    public List<FileQuerySearchResponseDTO> queryAllFile(){
        return queryFactory
                .select(Projections.constructor(FileQuerySearchResponseDTO.class, file.fileId, file.name, file.createdAt, file.fileType, file.path))
                .from(file)
                .orderBy(file.name.asc(), file.createdAt.asc())
                .fetch();
    }

    public Page<FileQuerySearchResponseDTO> queryFilesByPage(FileQueryRequestDTO fileQueryRequestDTO, Pageable pageable){
        List<FileQuerySearchResponseDTO> content = queryFactory
                .select(Projections.constructor(FileQuerySearchResponseDTO.class, file.fileId, file.name, file.createdAt, file.fileType, file.path))
                .from(file)
                .orderBy("desc".equals(fileQueryRequestDTO.getName()) ? file.name.desc() : file.name.asc(),
                        "desc".equals(fileQueryRequestDTO.getTime()) ? file.createdAt.desc() : file.createdAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(file.count())
                .from(file);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    public Page<FileQuerySearchResponseDTO> searchFilesByCriteria(FileSearchRequestDTO fileSearchRequestDTO, Pageable pageable){
        List<FileQuerySearchResponseDTO> content = queryFactory
                .select(Projections.constructor(FileQuerySearchResponseDTO.class, file.fileId, file.name, file.createdAt, file.fileType, file.path))
                .from(file)
                .where(nameContains(fileSearchRequestDTO.getName()),
                        pathEq(fileSearchRequestDTO.getPath()),
                        createdAtLoe(fileSearchRequestDTO.getBefore()),
                        createdAtGoe(fileSearchRequestDTO.getAfter()),
                        fileTypeEq(fileSearchRequestDTO.getFileType()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(file.count())
                .from(file)
                .where(nameContains(fileSearchRequestDTO.getName()),
                        pathEq(fileSearchRequestDTO.getPath()),
                        createdAtLoe(fileSearchRequestDTO.getBefore()),
                        createdAtGoe(fileSearchRequestDTO.getAfter()),
                        fileTypeEq(fileSearchRequestDTO.getFileType()));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    public String findNameById(Long fileId) {
        return queryFactory
                .select(file.name)
                .from(file)
                .where(file.fileId.eq(fileId))
                .fetchOne();
    }

    private BooleanExpression nameContains(String name) {
        NumberTemplate<Double> nameContainsFullText = Expressions.numberTemplate(Double.class,
                "function('match_against', {0}, {1})", file.name, "\"" +name + "\"");

        return (name != null) ? nameContainsFullText.gt(0) : null;
    }

    private BooleanExpression pathEq(String path) {
        return (path != null) ? file.path.eq(path) : null;
    }

    private BooleanExpression createdAtLoe(LocalDate before) {
        return (before != null) ? file.createdAt.loe(before.atTime(0,0)) : null;
    }

    private BooleanExpression createdAtGoe(LocalDate after) {
        return (after != null) ? file.createdAt.goe(after.atTime(0,0)) : null;
    }

    private BooleanExpression fileTypeEq(String fileType) {
        return (fileType != null) ? file.fileType.eq(fileType) : null;
    }
}
