from flask import Flask, render_template, request

app = Flask(__name__)

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/submit', methods=['POST'])
def submit():
    title = request.form.get('title')
    comments = request.form.get('comments')


    print(f'Title: {title}')
    print(f'Comments: {comments}')

    return "잘됐네요 !!"

if __name__ == '__main__':
    app.run(debug=True)
