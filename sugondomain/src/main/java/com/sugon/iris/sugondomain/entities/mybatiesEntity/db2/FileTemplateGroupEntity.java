package com.sugon.iris.sugondomain.entities.mybatiesEntity.db2;

import com.sugon.iris.sugondomain.beans.fileBeans.FileTemplateGroupBean;
import lombok.Data;

import java.util.Objects;

@Data
public class FileTemplateGroupEntity extends FileTemplateGroupBean {


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileTemplateGroupEntity that = (FileTemplateGroupEntity) o;
        return Objects.equals(this.getGroupId(), that.getGroupId()) &&
                Objects.equals(this.getGroupName(), that.getGroupName()) &&
                Objects.equals(this.getUserId(), that.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getGroupId(), this.getGroupName(), this.getUserId());
    }
}
