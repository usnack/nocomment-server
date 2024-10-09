package io.usnack.nocomment.notion.repository;

import io.usnack.nocomment.notion.repository.entity.NotionBlockMeta;
import org.springframework.data.repository.CrudRepository;

public interface NotionBlockMetaRepository extends CrudRepository<NotionBlockMeta, String> {
}
