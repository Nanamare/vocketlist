## VocketList 안드로이드 Branch 정의
크게 master, develop 2가지의 branch를 갖고 있으며 이 두가지 branch는 절대 삭제가 되어서는 안되며, 만일 삭제가 필요한 경우 팀원들과 협의하여 진행하여야 한다.

> remote 저장소를 기준으로 설명되었으며 local 저장소 내에서는 자유롭게 사용하면 된다.

### master
1. 가장 중요한 branch로써 master branch로부터 생성된 APK 파일을 마켓에 배포하여야 한다.
2. master branch는 develop branch로부터 작업된 내용을 merge만을 하며 직접 수정한 내용을 commit 해서는 안된다.

### develop
1. develop branch는 향후 배포될 내용이 포함된 branch이다.
2. 그러므로 개발된 내용은 develop 기준으로 merge되어야 한다.
3. 특별한 경우가 아닌 이상 수정된 사항은 PR(Pull Requset)를 통해서 진행한다.

### 개발 branch
1. 개발 branch는 원하는 지점으로부터 branch를 생성하여 진행할 수 있다.
2. 다만 이를 반영하기 위해서는 develop을 기준으로 PR(Pull Request)를 통해 진행한다.


## PR(Pull Request) 규칙
### PR시 주의 사항
1. commit이 많은 경우 불필요한 commit들을 sqush를 이용하여 통합한다.
> 명령 : `git rebase -i HEAD~[COUNT]`

2. develop 기준으로 rebase가 되어 있어야 한다.
> 명령 : `git rebase develop`

3. 위 작업을 다 하였으면 remote server로 push 한다.
4. 리뷰어를 1명 이상 지정하여야 한다.
5. 수정 사항이 생기면 `1 ~ 3번` 과정을 반복한다.
6. 만일 리뷰어가 리뷰를 진행하지 않고 있는 경우 리뷰어에게 구두 혹은 메신저로 요청하여 PR이 계속 남아 있지 않게 한다.
7. PR은 기능 단위별로 올리고 만일 큰 수정 사항인 경우는 기능을 나누어 PR 올리도록 한다.