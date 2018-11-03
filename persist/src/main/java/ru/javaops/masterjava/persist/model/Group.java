package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class Group extends BaseEntity{
    private @NonNull String name;
    private @NonNull GroupType type;
    private @NonNull @Column("project_id") int projectId;

    public Group(Integer id, String name,GroupType type, int projectId){
        this(name,type, projectId);
        this.id = id;
    }
}
