usage:
	@echo "========================================================================================="
	@echo "usage             : 显示本菜单"
	@echo "clean             : 清理项目"
	@echo "compile           : 编译源文件"
	@echo "package           : 构建jar文件"
	@echo "docker            : 构建docker镜像"
	@echo "dist              : 构建jar文件，构建docker镜像并打包"
	@echo "tree-dependencies : 分析包依赖树"
	@echo "github            : 推送文件到github.com"
	@echo "version           : 更改版本号"
	@echo "========================================================================================="

clean:
	@mvn clean -q

compile:
	@mvn clean compile -Dmaven.test.skip=true

package:
	@mvn clean package -Dmaven.test.skip=true

docker:
	@mvn clean package antrun:run@build-dockerimage antrun:run@tar-dockerimage -Dmaven.test.skip=true

dist:
	@mvn clean package antrun:run@build-dockerimage antrun:run@tar-dockerimage assembly:single@distro-assembly -Dmaven.test.skip=true
	@mvn clean -q

tree-dependencies:
	@mvn dependency:tree

version:
	@mvn -f $(CURDIR)/pom.xml versions:set
	@mvn -f $(CURDIR)/pom.xml -N versions:update-child-modules
	@mvn -f $(CURDIR)/pom.xml versions:commit

github: clean
	@git status
	@git add .
	@git commit -m "$(shell /bin/date "+%F %T")"
	@git push

.PHONY: usage clean compile package docker dist tree-dependencies version github
