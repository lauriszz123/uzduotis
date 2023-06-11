dist_folder := ./dist
build_folder := ./build
main_src := ${build_folder}/libs/uzduotis-0.0.1-SNAPSHOT.jar
test_reports := ${build_folder}/reports/tests/test
swagger_docs := ./swagger/
JS_FILE := ${dist_folder}/docs/swagger-initializer.js
OPENAPI_FILE := openapi.json

all: clean build prod

clean:
	rm -rf ${dist_folder}
	rm -rf ${build_folder}

build:
	./gradlew build

prod:
	mkdir ${dist_folder}
	mkdir ${dist_folder}/jar
	mkdir ${dist_folder}/test_report
	mkdir ${dist_folder}/docs
	
	mv ${main_src} ${dist_folder}/jar/uzduotis.jar
	mv ${test_reports}/* ${dist_folder}/test_report/.
	cp -R ${swagger_docs} ${dist_folder}/docs/.

	OPENAPI_CONTENT=$$(cat $(OPENAPI_FILE)); \
    OPENAPI_CONTENT_ESCAPED=$$(echo "$$OPENAPI_CONTENT" | sed 's/\//\\\//g'); \
    sed "s/SPEC_FILE/$$OPENAPI_CONTENT_ESCAPED/g" $(JS_FILE) > $(JS_FILE).new && \
    mv $(JS_FILE).new $(JS_FILE)

	cp custom.properties ${dist_folder}/jar/application.properties
