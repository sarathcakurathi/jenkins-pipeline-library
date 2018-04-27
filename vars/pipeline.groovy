def call(body) {
    def pipelineParams= [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = pipelineParams
    body()
	node {
		try {
			sh 'echo Params: pipelineParams'
			stage('Clone') {
				git branch: pipelineParams.branch, 
				url: pipelineParams.scmUrl
			}
			
			stage('Build') {
				sh './build.sh'
			}
		}
		catch (exception) {
			// If there was an exception thrown, the build failed
			currentBuild.result = "FAILED"
			throw exception
		} finally {
			cleanWs()
		}
	}
}