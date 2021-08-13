export default (name) => () => import(`@/pages/${name}`)
