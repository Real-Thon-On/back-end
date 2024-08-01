package com.fizz.fizz_server.domain.notify.domain;

import com.fizz.fizz_server.domain.user.domain.User;
import com.fizz.fizz_server.global.base.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Notify extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_id")
	private Long id;

	private String content;

	@Column(nullable = false)
	private Boolean isRead;

	private String time;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private NotificationType notificationType;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User receiver;

	private String videoUrl;
	private String thumbnailUrl;

	// TODO. 추후 알림이 확장된다면..
//	@ElementCollection
//	private Map<String, String> additionalInfo = new HashMap<>(); // 알림에 대한 추가 정보

	@Builder
	public Notify(String content, Boolean isRead, String time, NotificationType notificationType, User receiver, String videoUrl, String thumbnailUrl) {
		this.content = content;
		this.isRead = isRead;
		this.time = time;
		this.notificationType = notificationType;
		this.receiver = receiver;
		this.videoUrl = videoUrl;
		this.thumbnailUrl = thumbnailUrl;
	}
}
